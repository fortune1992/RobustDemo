package com.freebrio.robustdemo.patch

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class PatchCheckerService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        Log.d("pengguolong", "start onHandle work: " + Thread.currentThread().name)
        Thread.sleep(10000)
        Log.d("pengguolong", "end onHandle work: " + Thread.currentThread().name)
//        PatchManager.requestPatch()
    }

    companion object {
        private const val JOB_ID = 1001

        fun enqueueWork(context: Context, workIntent: Intent) {
            enqueueWork(context, PatchCheckerService::class.java, JOB_ID, workIntent)
        }
    }
}
