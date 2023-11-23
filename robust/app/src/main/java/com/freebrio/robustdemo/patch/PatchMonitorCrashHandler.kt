package com.freebrio.robustdemo.patch

import android.content.Intent
import android.os.Process
import android.text.TextUtils
import android.util.Log
import com.freebrio.robustdemo.MyApplication
import com.freebrio.robustdemo.util.FileUtils
import com.freebrio.robustdemo.util.ThrowableUtils

import java.io.File

object PatchMonitorCrashHandler : Thread.UncaughtExceptionHandler {

    private var defaultCrashHandler: Thread.UncaughtExceptionHandler? = null

    private val crashFolder = File(MyApplication.getContext().filesDir, "patchCrash")

    fun init() {
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!repeatCrash(e)) {
            saveLastThrowable(e)
            val intent = Intent(MyApplication.getContext(), PatchCheckerService::class.java)
            PatchCheckerService.enqueueWork(MyApplication.getContext(), intent)
        }
        if (defaultCrashHandler != null) {
            defaultCrashHandler!!.uncaughtException(t, e)
        } else {
            Process.killProcess(Process.myPid())
        }
    }

    private fun repeatCrash(throwable: Throwable): Boolean {
        try {
            val lastThrowable = FileUtils.readFile2String(File(crashFolder, "throwable"))
            if (!TextUtils.isEmpty(lastThrowable)) {
                val timeThrowable = lastThrowable.split("#".toRegex(), 2).toTypedArray()
                val lastTime = java.lang.Long.parseLong(timeThrowable[0])
                if (System.currentTimeMillis() - lastTime < 5 * 60 * 1000) {
                    if (TextUtils.equals(ThrowableUtils.getFullStackTrace(throwable), timeThrowable[1])) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "check repeatCrash error!!" + e.message)
        }

        return false
    }

    private fun saveLastThrowable(throwable: Throwable) {
        FileUtils.createOrExistsDir(crashFolder)
        FileUtils.writeFileFromString(File(crashFolder, "throwable"), getThrowableString(throwable))
    }

    private fun getThrowableString(throwable: Throwable): String {
        val time = System.currentTimeMillis()
        val s = ThrowableUtils.getFullStackTrace(throwable)
        return "$time#$s"
    }
}
