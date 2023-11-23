package com.freebrio.robustdemo.network;

/**
 * @author pengguolong
 * created 2023-06-28 17:04:14
 */
public class RetrofitServiceFactory {
    public static CommonService getCommonService() {
        return RetrofitAdapter.getService(CommonService.class, GeneralServiceInfo.class);
    }
}
