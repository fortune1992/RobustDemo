package com.bikan.reading.patch

import com.bikan.base.utils.OkHttpUtil
import com.xiaomi.bn.utils.coreutils.FileUtils
import okhttp3.Request
import java.io.File

fun downloadFileSync(url: String, file: File): Boolean {
    try {
        val response = OkHttpUtil.getInstance().defaultOkHttpClient.newCall(Request.Builder().url(url).build()).execute()
        if (response.isSuccessful) {
            FileUtils.copyToFile(response.body()?.byteStream(), file)
            return true
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return false
}