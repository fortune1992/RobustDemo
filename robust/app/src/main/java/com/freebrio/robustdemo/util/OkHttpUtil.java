package com.freebrio.robustdemo.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.freebrio.robustdemo.BuildConfig;
import com.freebrio.robustdemo.MyApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.Util;

public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private final OkHttpClient mClient;
    private String userAgent = "";
    private final ExecutorService mExecutorService;

    public static OkHttpUtil getInstance() {
        return OkHttpUtilHolder.INSTANCE;
    }

    private OkHttpUtil() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//        List<IDefaultHttpClientInterceptor> interceptors = Router.getAllServices(IDefaultHttpClientInterceptor.class);
//        if (interceptors != null && interceptors.size() > 0) {
//            for (IDefaultHttpClientInterceptor interceptor : interceptors) {
//                Interceptor realInterceptor = interceptor.getDefaultInterceptor();
//                if (realInterceptor != null) {
//                    clientBuilder.addInterceptor(realInterceptor);
//                }
//            }
//        }
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(SystemInfo.isDebugMode() ? HttpLoggingInterceptor.Level.BODY
//                : HttpLoggingInterceptor.Level.NONE);
        mExecutorService = new ThreadPoolExecutor(0, 128, 60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), Util.threadFactory("OkHttp Dispatcher", false),
                new DiscardNewPolicy());
        mClient = clientBuilder
//                .addNetworkInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .removeHeader("User-Agent")
                            .addHeader("User-Agent", getUserAgent())
                            .build();
                    return chain.proceed(request);
                })
                .dispatcher(new Dispatcher(getLimitedMaxPoolSizeExecutor()))
                .build();
    }

    public OkHttpClient getDefaultOkHttpClient() {
        return mClient;
    }

    public String getUserAgent() {
        if (TextUtils.isEmpty(userAgent)) {
            userAgent = MyApplication.getContext().getPackageName() + "/" +
                    BuildConfig.VERSION_NAME +
                    "(Linux; " +
                    "Android " + Build.VERSION.SDK_INT + ";" +
                    Build.MANUFACTURER + ";" +
                    Build.MODEL + ";" +
                    ")";
        }
        return userAgent;
    }

    public ExecutorService getLimitedMaxPoolSizeExecutor() {
        return mExecutorService;
    }

    private static class DiscardNewPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            Log.e(TAG, "DiscardPolicy rejectedExecution, r: " + r + " ; ThreadPoolExecutor: " + e);
        }
    }

    private static class OkHttpUtilHolder {
        private static final OkHttpUtil INSTANCE = new OkHttpUtil();
    }

    public interface IDefaultHttpClientInterceptor {
        Interceptor getDefaultInterceptor();
    }
}
