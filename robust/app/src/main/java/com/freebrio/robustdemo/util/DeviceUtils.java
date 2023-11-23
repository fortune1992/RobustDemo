package com.bikan.reading.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xiaomi.bn.utils.coreutils.ApplicationStatus;
import com.xiaomi.bn.utils.coreutils.DeviceIdHelper;
import com.xiaomi.bn.utils.coreutils.SharePrefUtils;
import com.xiaomi.bn.utils.coreutils.SystemPropertieUtils;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class DeviceUtils {

    private static final String UNKNOWN = "unknown";
    private static final String UNIQUE_ANDROID_ID = "unique_android_id";

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    public static final Float DENSITY = ApplicationStatus.getApplicationContext().getResources().getDisplayMetrics().density;

    /**
     * 设备IMEI号的MD5 避免直接传输设备IMEI号
     */
    private static String sImeiByMiui;

    @SuppressLint("MissingPermission")
    private static String getImeiByMiui() {
        if (!TextUtils.isEmpty(sImeiByMiui)) {
            return sImeiByMiui;
        }
        try {
            Class<?> clazz = Class.forName("miui.telephony.TelephonyManager");
            Method defaultMethod = clazz.getDeclaredMethod("getDefault");
            defaultMethod.setAccessible(true);
            Object telephonyManager = defaultMethod.invoke(null);
            Method method = clazz.getDeclaredMethod("getSmallDeviceId");
            method.setAccessible(true);
            sImeiByMiui = (String) method.invoke(telephonyManager);
            return sImeiByMiui;
        } catch (Exception e) {
            return "";
        }
    }

    private static String sVersionCode;
    private static String sVersionName;
    private static String sCarrierOperator;

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
            PackageInfo info = ApplicationStatus.getApplicationContext().getPackageManager()
                    .getPackageInfo(ApplicationStatus.getApplicationContext().getPackageName(), 0);
            sVersionCode = String.valueOf(info.versionCode);
            sVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            sVersionCode = "1";
            sVersionName = "1.0";
        }
    }

    /**
     * 得到sim的运营商信息 对于双卡双待手机，暂时只考虑mtk芯片
     *
     * @return
     */
    public static String getCarrierOperator() {
        if (sCarrierOperator != null) {
            return sCarrierOperator;
        }

        Method method = null;
        Object result_0 = null;
        Object result_1 = null;
        StringBuilder operator = new StringBuilder();
        TelephonyManager tm = (TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(
                Context.TELEPHONY_SERVICE);
        try {
            method = TelephonyManager.class.getMethod("getSimOperatorGemini", new Class[]{
                    int.class
            });
            result_0 = method.invoke(tm, new Object[]{
                    Integer.valueOf(0)
            });
            result_1 = method.invoke(tm, new Object[]{
                    Integer.valueOf(1)
            });
        } catch (Exception e) {
            String ret = tm.getSimOperator();
            if (TextUtils.isEmpty(ret)) {
                ret = UNKNOWN;
            }
            sCarrierOperator = ret;
            return ret;
        }

        if (TextUtils.isEmpty(result_0.toString())) {
            operator.append(UNKNOWN).append(",");
        } else {
            operator.append(result_0.toString()).append(",");
        }

        if (TextUtils.isEmpty(result_1.toString())) {
            operator.append(UNKNOWN);
        } else {
            operator.append(result_1.toString());
        }
        sCarrierOperator = operator.toString();
        return sCarrierOperator;
    }

    public static boolean hasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) ApplicationStatus.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;
    }

    @SuppressLint({
            "MissingPermission", "HardwareIds"
    })
    private static String getImeiByAndroidAPI() {
        try {
            return ((TelephonyManager) ApplicationStatus.getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Throwable e) {
            return "";
        }
    }

    private static String getImeiByUtil() {
        return DeviceIdHelper.getRawImei1(ApplicationStatus.getApplicationContext());
    }

    public synchronized static String getUniqueID() {
        String uuid = SharePrefUtils.getString(ApplicationStatus.getApplicationContext(),
                UNIQUE_ANDROID_ID, "");
        if (TextUtils.isEmpty(uuid)) {
            String uniqueStr = Build.SERIAL;
            if (Build.UNKNOWN.equals(uniqueStr) || Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                uniqueStr = getAndroidId();
            }
            String stringBuilder = "BIKAN_UNIQUE_PREFIX" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) +
                    Build.CPU_ABI.length() % 10 +
                    (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) +
                    (Build.MODEL.length() % 10) +
                    (Build.PRODUCT.length() % 10);
            uuid = new UUID(stringBuilder.hashCode(), uniqueStr.hashCode()).toString();
            SharePrefUtils.putString(ApplicationStatus.getApplicationContext(), UNIQUE_ANDROID_ID,
                    uuid);
        }
        return uuid;
    }

    public static String getImei() {
        String imei;
        if (isMIUI()) {
            imei = getImeiByMiui();
            if (!TextUtils.isEmpty(imei)) {
                return imei;
            }
            imei = getImeiByUtil();
            if (!TextUtils.isEmpty(imei)) {
                return imei;
            }
        }
        imei = getImeiByAndroidAPI();
        return imei == null ? "" : imei;
    }

    public static String getLocale() {
        Locale defaultLocale = Locale.getDefault();
        return defaultLocale != null ? Locale.getDefault().toString() : "zh_CN";
    }

    public static String getRegion() {
        String region = SystemPropertieUtils.get("ro.miui.region", "");
        if (TextUtils.isEmpty(region)) {
            region = SystemPropertieUtils.get("ro.product.locale.region", "");
        }
        if (TextUtils.isEmpty(region)) {
            region = SystemPropertieUtils.get("persist.sys.country", "");
        }
        if (TextUtils.isEmpty(region)) {
            region = Locale.getDefault().getCountry();
        }
        return region;
    }

    public static String getCarrierName() {
        String carrier = SystemPropertieUtils.get("ro.carrier.name", "");
        return carrier;
    }

    public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        if (device.equals("Xiaomi")) {
            return !TextUtils.isEmpty(SystemPropertieUtils.get(KEY_MIUI_VERSION_CODE))
                    || !TextUtils.isEmpty(SystemPropertieUtils.get(KEY_MIUI_VERSION_NAME))
                    || !TextUtils.isEmpty(SystemPropertieUtils.get(KEY_MIUI_INTERNAL_STORAGE));
        } else {
            return false;
        }
    }

    public static boolean isHuawei() {
        String device = Build.MANUFACTURER;
        return device != null && device.toLowerCase().equals("huawei");
    }

    public static String getAndroidId() {
        String androidId = Settings.System.getString(
                ApplicationStatus.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId)) {
            androidId = androidId.toLowerCase();
        }
        return androidId;
    }

    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166</p>
     * <p>电信：133、153、173、177、180、181、189、199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(REGEX_MOBILE_EXACT, input);
    }

    private static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    public static boolean isRestrictImei() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.P &&
                "V12".equalsIgnoreCase(getMiuiVersion()) &&
                "1".equals(SystemPropertieUtils.get("ro.miui.restrict_imei_p", "0"));
    }

    public static String getMiuiVersion() {
        return SystemPropertieUtils.get("ro.miui.ui.version.name", "");
    }

    public static Boolean isRestrictVersion() {
        return Build.VERSION.SDK_INT >= 29 ||
                (Build.VERSION.SDK_INT == Build.VERSION_CODES.P && "V12".equalsIgnoreCase(getMiuiVersion()));
    }

    public static String getSerialNumber() {
        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            // (?) Lenovo Tab (https://stackoverflow.com/a/34819027/1276306)
            serialNumber = (String) get.invoke(c, "gsm.sn1");

            if (serialNumber.equals("")) {
                // Samsung Galaxy S5 (SM-G900F) : 6.0.1
                // Samsung Galaxy S6 (SM-G920F) : 7.0
                // Samsung Galaxy Tab 4 (SM-T530) : 5.0.2
                // (?) Samsung Galaxy Tab 2 (https://gist.github.com/jgold6/f46b1c049a1ee94fdb52)
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            }

            if (serialNumber.equals("")) {
                // Archos 133 Oxygen : 6.0.1
                // Google Nexus 5 : 6.0.1
                // Hannspree HANNSPAD 13.3" TITAN 2 (HSG1351) : 5.1.1
                // Honor 5C (NEM-L51) : 7.0
                // Honor 5X (KIW-L21) : 6.0.1
                // Huawei M2 (M2-801w) : 5.1.1
                // (?) HTC Nexus One : 2.3.4 (https://gist.github.com/tetsu-koba/992373)
                serialNumber = (String) get.invoke(c, "ro.serialno");
            }

            if (serialNumber.equals("")) {
                // (?) Samsung Galaxy Tab 3 (https://stackoverflow.com/a/27274950/1276306)
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            }

            if (serialNumber.equals("")) {
                // Archos 133 Oxygen : 6.0.1
                // Hannspree HANNSPAD 13.3" TITAN 2 (HSG1351) : 5.1.1
                // Honor 9 Lite (LLD-L31) : 8.0
                // Xiaomi Mi 8 (M1803E1A) : 8.1.0
                serialNumber = Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = "";
        }
        if (serialNumber == null) {
            serialNumber = "";
        }
        serialNumber = serialNumber.replaceAll("(?i)unknown", "");
        return serialNumber;
    }
}
