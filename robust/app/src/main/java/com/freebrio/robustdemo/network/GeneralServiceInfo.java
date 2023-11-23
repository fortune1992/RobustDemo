package com.freebrio.robustdemo.network;


import java.util.HashMap;
import java.util.Map;

import okhttp3.Dispatcher;
import okhttp3.Request;

public class GeneralServiceInfo implements IServiceInfo {
    private static final Dispatcher GENERAL_DISPATCHER = new Dispatcher();

    @Override
    public String getBaseUrl() {
        return "https://feed.browser.miui.com";
    }

    @Override
    public Map<String, String> getCommonParameters() {
        return new HashMap<>();
    }

    @Override
    public Map<String, String> getExtraParameters(Request request) {
        return getSign(request);
    }

    private Map<String, String> getSign(Request request) {
        return new HashMap<>();
    }

    @Override
    public Dispatcher getDispatcher() {
        return GENERAL_DISPATCHER;
    }
}
