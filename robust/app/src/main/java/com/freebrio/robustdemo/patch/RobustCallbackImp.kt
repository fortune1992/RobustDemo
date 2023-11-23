package com.freebrio.robustdemo.patch

import android.util.Log

class RobustCallbackImp : IRobustCallBack {
    private val moduleName = "robust"

    override fun onPatchListFetched(result: Boolean, isNet: Boolean, patches: List<AppPatch>?) {
        if (isNet) {
//            UploadLogProxy.uploadElkData(moduleName, "patchFetch", isNet.toString(),
//                patches?.size?.toString(), null, System.currentTimeMillis(), null)
        }
        Log.d(TAG, "onPatchListFetched result: $result; isNet: $isNet; patches size: ${patches?.size}")
    }

    override fun onPatchFetched(result: Boolean, isNet: Boolean, patch: AppPatch?) {
        Log.d(TAG, "onPatchFetched result: $result; isNet: $isNet; patch:${patch?.name}")
    }

    override fun onPatchApplied(result: Boolean, patch: AppPatch?) {
        if (!hasPatchLogUpload()) {
//            UploadLogProxy.uploadElkData(moduleName, "patchApply", result.toString(),
//                patch?.md5, null, System.currentTimeMillis()) {
//                setPatchLogUpload(it && result)
//            }
        }
        Log.d(TAG, "onPatchApplied result: $result onPatchApplied patch: ${patch?.name}")
    }

    override fun logNotify(log: String?) {
        Log.d(TAG, "logNotify log: $log")
    }

    override fun exceptionNotify(throwable: Throwable?, where: String?) {
        throwable?.printStackTrace()
        Log.e(TAG, "exceptionNotify where: $where", throwable)
    }
}
