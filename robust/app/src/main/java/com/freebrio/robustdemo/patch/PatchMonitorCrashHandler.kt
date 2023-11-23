package com.bikan.reading.patch

import android.content.Intent
import android.os.Process
import android.text.TextUtils

import com.xiaomi.bn.utils.coreutils.ApplicationStatus
import com.xiaomi.bn.utils.coreutils.FileUtils
import com.xiaomi.bn.utils.coreutils.ThrowableUtil
import com.xiaomi.bn.utils.logger.Logger

import java.io.File

object PatchMonitorCrashHandler : Thread.UncaughtExceptionHandler {

    private var defaultCrashHandler: Thread.UncaughtExceptionHandler? = null

    private val crashFolder = File(ApplicationStatus.getApplicationContext().filesDir, "patchCrash")

    fun init() {
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!repeatCrash(e)) {
            saveLastThrowable(e)
            val intent = Intent(ApplicationStatus.getApplicationContext(), PatchCheckerService::class.java)
            ApplicationStatus.getApplicationContext().startService(intent)
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
                    if (TextUtils.equals(ThrowableUtil.throwableToString(throwable), timeThrowable[1])) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            Logger.e("check repeatCrash error!!" + e.message)
        }

        return false
    }

    private fun saveLastThrowable(throwable: Throwable) {
        FileUtils.createOrExistsDir(crashFolder)
        FileUtils.writeFileFromString(File(crashFolder, "throwable"), getThrowableString(throwable))
    }

    private fun getThrowableString(throwable: Throwable): String {
        val time = System.currentTimeMillis()
        val s = ThrowableUtil.throwableToString(throwable)
        return "$time#$s"
    }
}
