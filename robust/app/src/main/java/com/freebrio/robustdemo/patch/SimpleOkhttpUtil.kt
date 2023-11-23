package com.freebrio.robustdemo.patch

import com.freebrio.robustdemo.util.FileUtils
import com.freebrio.robustdemo.util.OkHttpUtil
import okhttp3.Request
import java.io.File

fun downloadFileSync(url: String, file: File): Boolean {
    try {
        val response = OkHttpUtil.getInstance().defaultOkHttpClient.newCall(Request.Builder().url(url).build()).execute()
        if (response.isSuccessful) {
            FileUtils.writeFileFromIS(file, response.body?.byteStream())
            return true
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return false
}