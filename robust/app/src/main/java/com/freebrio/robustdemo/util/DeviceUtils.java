package com.freebrio.robustdemo.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.freebrio.robustdemo.MyApplication;

public class DeviceUtils {

    private static String sVersionCode;
    private static String sVersionName;

    public static String getAppVersionName() {
        if (sVersionName == null) {
            initVersionInfo();
        }
        return sVersionName;
    }

    public static String getAppVersionCode() {
        if (sVersionCode == null) {
            initVersionInfo();
        }
        return sVersionCode;
    }

    private static void initVersionInfo() {
        try {
            PackageInfo info = MyApplication.getContext().getPackageManager()
                    .getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            sVersionCode = String.valueOf(info.versionCode);
            sVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            sVersionCode = "1";
            sVersionName = "1.0";
        }
    }
}
