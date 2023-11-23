package com.freebrio.robustdemo.network;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;

import com.freebrio.robustdemo.util.OkHttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Function;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitAdapter {

    private static final Object LOCK = new Object();
    // ApiServiceClass.hashCode -> <ApiServiceClass, ApiServiceInstance>
    private static final HashMap<Integer, HashMap<Class, Object>> SERVICE_MAP = new HashMap<>();
    // ApiServiceClass.hashCode -> ApiServiceInfo
    private static final HashMap<Integer, IServiceInfo> SERVICE_INFO_MAP = new HashMap<>();

    public static <T> T getService(Class<T> apiClass, Class<? extends IServiceInfo> serviceInfo) {
        synchronized (LOCK) {
            int type = apiClass.hashCode();
            if (SERVICE_INFO_MAP.get(type) == null) {
                putIfAbsent(SERVICE_INFO_MAP, type, serviceInfo::newInstance);
            }
            if (SERVICE_MAP.get(type) == null) {
                putIfAbsent(SERVICE_MAP, type, HashMap::new);
            }
            if (SERVICE_MAP.get(type).get(apiClass) == null) {
                putIfAbsent(Objects.requireNonNull(SERVICE_MAP.get(type)), apiClass, () -> RetrofitAdapter.createService(apiClass));
            }
            return (T) SERVICE_MAP.get(type).get(apiClass);
        }
    }

    /**
     * 重建service，清除ConnectionPool
     *
     * @param host
     */
    public static void resetService(String host) {
        if (TextUtils.isEmpty(host)) {
            return;
        }
        synchronized (LOCK) {
            Set<Integer> keys = SERVICE_INFO_MAP.keySet();
            for (Integer key : keys) {
                IServiceInfo serviceInfo = SERVICE_INFO_MAP.get(key);
                if (TextUtils.equals(Uri.parse(serviceInfo.getBaseUrl()).getHost(), host)) {
                    SERVICE_MAP.remove(key);
                }
            }
        }
    }

    private static <T> T createService(Class<T> apiClass) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(chain -> proceedRequest(chain, chain1 -> {
            HttpUrl url = chain1.request().url();
            HttpUrl.Builder builder = url.newBuilder();
            Map<String, String> params = getServiceInfo(apiClass).getCommonParameters();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String s = url.queryParameter(entry.getKey());
                    if (s == null) {
                        builder.addQueryParameter(entry.getKey(), entry.getValue());
                    }
                }
            }
            return builder.build();
        }));
        clientBuilder.connectTimeout(Constants.CONNECT_TIMEOUT_MILLIS, TimeUnit.SECONDS);
        clientBuilder.readTimeout(Constants.READ_TIMEOUT_MILLIS, TimeUnit.SECONDS);
        clientBuilder.dispatcher(new Dispatcher(OkHttpUtil.getInstance().getLimitedMaxPoolSizeExecutor()));

        Interceptor interceptor = getServiceInfo(apiClass).getInterceptor();
        if (interceptor != null) {
            clientBuilder.addInterceptor(interceptor);
        }

        clientBuilder.addInterceptor(chain -> appendFormBody(apiClass, chain));
        // clientBuilder.addInterceptor(RetrofitAdapter::checkImeiOrOaid);
        clientBuilder.addInterceptor(chain ->
                proceedRequest(chain, chain1 -> {
                    HttpUrl.Builder builder = chain1.request().url().newBuilder();
                    Map<String, String> extraParams = getServiceInfo(apiClass).getExtraParameters(chain1.request());
                    if (extraParams != null) {
                        for (Map.Entry<String, String> entry : extraParams.entrySet()) {
                            builder.addQueryParameter(entry.getKey(), entry.getValue());
                        }
                    }
                    return builder.build();
                }));
        clientBuilder.addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", OkHttpUtil.getInstance().getUserAgent())
                    .build();
            return chain.proceed(request);
        });

        addDispatcher(clientBuilder, apiClass);
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(getServiceInfo(apiClass).getBaseUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());
        return retrofitBuilder.build().create(apiClass);
    }

    private static void addDispatcher(OkHttpClient.Builder clientBuilder, Class<?> apiClass) {
        int type = apiClass.hashCode();
        IServiceInfo serviceInfo = SERVICE_INFO_MAP.get(type);
        if (serviceInfo != null && serviceInfo.getDispatcher() != null) {
            clientBuilder.dispatcher(serviceInfo.getDispatcher());
        }
    }


    private static Response appendFormBody(Class<?> apiClass, Interceptor.Chain chain)
            throws IOException {
        Request request = chain.request();
        Pair<String, Function<String, String>> pair = getServiceInfo(apiClass).getFormBody();
        if (pair == null) {
            return chain.proceed(request);
        }

        RequestBody body = request.body();
        if (!(body instanceof FormBody)) {
            return chain.proceed(request);
        }
        FormBody formBody = (FormBody) body;
        int size = formBody.size();
        if (size == 0) {
            return chain.proceed(request);
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (int i = 0; i < size; i++) {
            String name = formBody.name(i);
            String value = formBody.value(i);
            if (Objects.equals(pair.first, name)) {
                try {
                    builder.add(name, pair.second.apply(value));
                } catch (Exception e) {
                    builder.add(name, value);
                    e.printStackTrace();
                }
            } else {
                builder.add(name, value);
            }
        }
        request = request.newBuilder().post(builder.build()).build();
        return chain.proceed(request);
    }

    private static Response proceedRequest(Interceptor.Chain chain,
            Function<Interceptor.Chain, HttpUrl> cb) throws IOException {
        try {
            HttpUrl url = cb.apply(chain);
            Request request = chain.request().newBuilder().url(url).build();
            return chain.proceed(request);
        } catch (Throwable ignored) {
            throw new IOException(ignored);
        }
    }

    private static IServiceInfo getServiceInfo(Class<?> apiClass) {
        synchronized (LOCK) {
            return SERVICE_INFO_MAP.get(apiClass.hashCode());
        }
    }

    private static <K, V> void putIfAbsent(HashMap<K, V> map, K key, Callable<V> create) {
        V v = map.get(key);
        if (v == null) {
            try {
                map.put(key, create.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
