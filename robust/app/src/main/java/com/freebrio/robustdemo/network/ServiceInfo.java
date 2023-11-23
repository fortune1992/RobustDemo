package com.bikan.base.net;

import android.Manifest;
import android.os.Build;
import android.text.TextUtils;

import com.bikan.base.BuildConfig;
import com.bikan.base.SystemInfo;
import com.bikan.base.manager.AMapLocationManager;
import com.bikan.base.sp.SPKt;
import com.bikan.base.utils.DeviceWrapperUtils;
import com.bikan.base.utils.OaidUtil;
import com.bikan.base.utils.SharePref;
import com.bikan.reading.utils.DeviceUtils;
import com.ishumei.smantifraud.SmAntiFraud;
import com.xiaomi.bn.utils.coreutils.ApplicationStatus;
import com.xiaomi.bn.utils.coreutils.GsonUtils;
import com.xiaomi.bn.utils.coreutils.NetworkUtils;
import com.xiaomi.bn.utils.coreutils.PackageUtils;
import com.xiaomi.bn.utils.coreutils.PermissionUtils;
import com.xiaomi.bn.utils.coreutils.ScreenUtils;
import com.xiaomi.bn.utils.coreutils.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.Request;

abstract public class ServiceInfo implements IServiceInfo {

    public static Map<String, String> getBaseCommonParameters() {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("version_name", BuildConfig.VERSION_NAME);
        mapParams.put("version_code", String.valueOf(BuildConfig.VERSION_CODE));
        mapParams.put("net", NetworkUtils.getNetworkTypeName());
        mapParams.put("ts", String.valueOf(System.currentTimeMillis()));
        mapParams.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        mapParams.put("os_type", "android");
        mapParams.put("imei", DeviceWrapperUtils.getDecisionImeiMd5());
        mapParams.put("realImei", DeviceWrapperUtils.getRealImeiMd5());
        mapParams.put("carrier", NetworkUtils.getCarrierName());
        mapParams.put("model", Build.MODEL);
        mapParams.put("screen_width", String.valueOf(ScreenUtils.getScreenWidth()));
        mapParams.put("screen_height", String.valueOf(ScreenUtils.getScreenHeight()));
        mapParams.put("screen_density", String.valueOf(ScreenUtils.getScreenDensity()));
        mapParams.put("app_channel", SystemInfo.getAppChannel());
        mapParams.put("appName", SystemInfo.getFakeAppName());
        getLoginStatus(mapParams);
        String imei1 = SharePref.getImei1();
        if (!TextUtils.isEmpty(imei1)) {
            mapParams.put("imeis", imei1);
        }
        if (AMapLocationManager.getInstance().getLocation() != null) {
            mapParams.put("lat", String.valueOf(AMapLocationManager.getInstance().getLocation().getLatitude()));
            mapParams.put("lon", String.valueOf(AMapLocationManager.getInstance().getLocation().getLongitude()));
        }
        mapParams.put("permissions", getPermissionJsonInfo());
        mapParams.put("oaid", OaidUtil.INSTANCE.getOaid());
        mapParams.put("vaid", OaidUtil.INSTANCE.getVaid());
        mapParams.put("aaid", OaidUtil.INSTANCE.getAaid());
        mapParams.put("app_dev", "android");
        mapParams.put("shumei_id", SmAntiFraud.getDeviceId());
        mapParams.put("nonce", UUID.randomUUID().toString());
        mapParams.put("userId", SharePref.getUserId());
        if (DeviceWrapperUtils.isMIUI()) {
            mapParams.put("restrictImei", String.valueOf(DeviceUtils.isRestrictImei()));
            mapParams.put("miuiVersionName", DeviceUtils.getMiuiVersion());
        }
        mapParams.put("abi", SystemInfo.getAbi());
        mapParams.putAll(getAdQueryMap());
        return mapParams;
    }

    public static void getLoginStatus(Map<String, String> mapParams) {
        int login = SPKt.getAccountPrefer().getInt(SharePref.LOGIN_STATUS);
        mapParams.put(SharePref.LOGIN_STATUS, String.valueOf(login));
    }

    public static Map<String, String> getAdQueryMap() {
        Map<String, String> map = new HashMap<>();
        map.put("isInter", "false");
        map.put("model", Build.MODEL);
        map.put("device", Build.DEVICE);
        map.put("androidVersion", Build.VERSION.RELEASE);
        if (DeviceWrapperUtils.isMIUI()) {
            map.put("miuiVersion", Build.VERSION.INCREMENTAL);
        } else {
            map.put("miuiVersion", "");
        }
        map.put("androidId", DeviceUtils.getAndroidId());
        map.put("networkType", String.valueOf(getNetType()));
        map.put("locale", DeviceUtils.getLocale());
        map.put("country", DeviceUtils.getRegion());
        map.put("packageName", ApplicationStatus.getApplicationContext().getPackageName());
        map.put("version", String.valueOf(BuildConfig.VERSION_CODE));
        String mimarketVersion = "-1";
        if (DeviceWrapperUtils.isMIUI()) {
            mimarketVersion = String.valueOf(PackageUtils.getVersionCode("com.xiaomi.market"));
        }
        map.put("mimarketVersion", mimarketVersion);
        return map;
    }

    private static int getNetType() {
        int networkType = NetworkUtils.getNetworkType();
        switch (networkType) {
            case NetworkUtils.NetworkType.NETWORK_NONE:
                return 0;
            case NetworkUtils.NetworkType.NETWORK_WIFI:
                return -1;
            default:
                return NetworkUtils.getTelephonyManager().getNetworkType();
        }
    }

    private static String getPermissionJsonInfo() {
        Map<String, Boolean> permissionInfoMap = new HashMap<>();
        permissionInfoMap.put("readPhoneState", PermissionUtils.isGranted(Manifest.permission.READ_PHONE_STATE));
        permissionInfoMap.put("accessLocation", PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION));
        permissionInfoMap.put("writeExternalStorage", PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        return GsonUtils.toJson(permissionInfoMap);
    }

    public static Map<String, String> getSign(Request request) {
        if (request == null) {
            return null;
        }

        TreeMap<String, String> paramsMap = new TreeMap<>();
        if (request.body() != null && request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();
            for (int i = 0; i < formBody.size(); i++) {
                paramsMap.put(URLDecoder.decode(formBody.encodedName(i)), URLDecoder.decode(formBody.encodedValue(i)));
            }
        }
        if (request.url() != null) {
            for (int i = 0; i < request.url().querySize(); i++) {
                paramsMap.put(request.url().queryParameterName(i), request.url().queryParameterValue(i));
            }
        }
        String key = StringUtils.getRequestKey(paramsMap);
        if (TextUtils.isEmpty(key)) {
            return null;
        } else {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("requestSign", key);
            return resultMap;
        }
    }
}
