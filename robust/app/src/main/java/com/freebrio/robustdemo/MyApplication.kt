package com.freebrio.robustdemo

import android.app.Application
import android.content.Context
import com.freebrio.robustdemo.patch.PatchManager

/**
 *
 * @author pengguolong
 * created 2023-06-28 15:53:04
 */
class MyApplication : Application() {

    companion object {

        private lateinit var sInstance: MyApplication

        @JvmStatic
        fun getInstance(): MyApplication {
            return sInstance
        }

        @JvmStatic
        fun getContext(): Context {
            return sInstance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this

        PatchManager.loadLocalPatch()

        // 同意隐私协议后请求远端patch文件
        if (true) {
            PatchManager.requestPatch()
        }
    }
}