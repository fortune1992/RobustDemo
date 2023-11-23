package com.bikan.base.net;

import android.util.Pair;

import java.util.Map;

import io.reactivex.functions.Function;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.Request;

public interface IServiceInfo {

    String getBaseUrl();

    default Map<String, String> getCommonParameters() {
        return null;
    }

    default Pair<String, Function<String, String>> getFormBody() {
        return null;
    }

    default Map<String, String> getExtraParameters(Request request) {
        return null;
    }

    default Interceptor getInterceptor() {
        return null;
    }

    default Dispatcher getDispatcher() {
        return null;
    }
}
