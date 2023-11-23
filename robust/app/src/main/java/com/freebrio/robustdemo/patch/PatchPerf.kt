package com.bikan.reading.patch

import android.text.TextUtils
import com.xiaomi.bn.utils.coreutils.ApplicationStatus
import com.xiaomi.bn.utils.coreutils.DeviceUtils
import com.xiaomi.bn.utils.coreutils.SharePrefUtils
import java.io.File

private const val PREF_NAME = "patch_pref"
private const val ROOT_DIR_NAME = "robust"
private const val KEY_PATCH_NAME = "patch_name"
private const val KEY_PATCH_HASH = "patch_hash"
private const val KEY_PATCH_APP_INIT = "patch_app_init"
private const val KEY_PATCH_UPLOAD_PREFIX = "patch_log_upload_"

/**
 * 本地是否有patch，patch name命名versionCode_patchVersion，如40780_v1
 * @return Boolean
 */
fun hasPatch(): Boolean {
    val patchName = getPatchName() ?: return false
    val version = patchName.split("_")[0]
    return if (version == DeviceUtils.getAppVersionCode()) {
        true
    } else {
        clearAll()
        false
    }
}

fun getPatchName(): String? {
    return SharePrefUtils.getString(ApplicationStatus.getApplicationContext(), PREF_NAME,
        KEY_PATCH_NAME, null)
}

fun setPatchName(patchVersion: String?) {
    SharePrefUtils.putString(ApplicationStatus.getApplicationContext(), PREF_NAME,
        KEY_PATCH_NAME, DeviceUtils.getAppVersionCode() + "_" + patchVersion)
}

fun getPatchHash(): String? {
    return SharePrefUtils.getString(ApplicationStatus.getApplicationContext(), PREF_NAME,
        KEY_PATCH_HASH, null)
}

fun setPatchHash(patchHash: String?) {
    SharePrefUtils.putString(ApplicationStatus.getApplicationContext(), PREF_NAME,
        KEY_PATCH_HASH, patchHash)
}

fun isPatchAppInit(): Boolean {
    return SharePrefUtils.getBoolean(ApplicationStatus.getApplicationContext(), PREF_NAME,
        KEY_PATCH_APP_INIT, false)
}

fun setPatchAppInit(patchAppInit: Boolean) {
    SharePrefUtils.putBoolean(ApplicationStatus.getApplicationContext(), PREF_NAME,
        KEY_PATCH_APP_INIT, patchAppInit)
}

/**
 * 获取当前版本的本地patch路径
 * @return String
 */
fun getPatchDirName(): String? {
    return getPatchRootDir() + File.separator + getPatchName()
}

fun getPatchFilePath(): String {
    return getPatchDirName() + File.separator + "patch.jar"
}

/**
 * 获取本地patch文件夹路径
 * @return String
 */
fun getPatchRootDir(): String? {
    val rootDirPath = ApplicationStatus.getApplicationContext().filesDir.toString() + File.separator + ROOT_DIR_NAME
    val rootDir = File(rootDirPath)
    if (!rootDir.exists()) {
        rootDir.mkdir()
    }
    return rootDirPath
}

/**
 * 清除patch
 *
 */
fun clearAll() {
    SharePrefUtils.clear(ApplicationStatus.getApplicationContext(), PREF_NAME)
}

/**
 * 当前版本patch成功的日志是否上传，用于减少日志上报
 * @return Boolean
 */
fun hasPatchLogUpload(): Boolean {
    val patchHash = getPatchHash()
    return if (TextUtils.isEmpty(patchHash)) {
        false
    } else {
        SharePrefUtils.getBoolean(ApplicationStatus.getApplicationContext(), PREF_NAME,
            KEY_PATCH_UPLOAD_PREFIX + patchHash, false)
    }
}

fun setPatchLogUpload(result: Boolean) {
    getPatchHash()?.let {
        SharePrefUtils.putBoolean(ApplicationStatus.getApplicationContext(), PREF_NAME,
            KEY_PATCH_UPLOAD_PREFIX + it, result)
    }
}
