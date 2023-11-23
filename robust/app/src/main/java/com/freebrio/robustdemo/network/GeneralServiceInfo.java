package com.bikan.base.net;

import com.bikan.base.utils.NetPreviewUtils;

import java.util.Map;

import okhttp3.Dispatcher;
import okhttp3.Request;

public class GeneralServiceInfo extends ServiceInfo {
    private static final Dispatcher GENERAL_DISPATCHER = new Dispatcher();

    @Override
    public String getBaseUrl() {
        if (NetPreviewUtils.isQAEnv()) {
            return "http://feed-test.browser.miui.com";
        } else if (NetPreviewUtils.isNetPreview()) {
            return "http://feed.dev.browser.miui.com";
        } else {
            return "https://feed.browser.miui.com";
        }
    }

    @Override
    public Map<String, String> getCommonParameters() {
        return getBaseCommonParameters();
    }

    @Override
    public Map<String, String> getExtraParameters(Request request) {
        return getSign(request);
    }

    @Override
    public Dispatcher getDispatcher() {
        return GENERAL_DISPATCHER;
    }
}
