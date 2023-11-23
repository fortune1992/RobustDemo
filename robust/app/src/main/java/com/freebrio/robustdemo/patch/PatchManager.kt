package com.freebrio.robustdemo.patch

import com.freebrio.robustdemo.MyApplication
import com.freebrio.robustdemo.util.FileUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object PatchManager {

    init {
        PatchMonitorCrashHandler.init()
    }

    fun checkPatch() {
        loadLocalPatch()
        requestPatch()
    }

    fun loadLocalPatch() {
        if (hasPatch()) {
            if (isPatchAppInit()) {
                loadPatch()
            } else {
                GlobalScope.launch {
                    loadPatch()
                }
            }
        }
    }

    fun requestPatch() {
        AppPatchExecutor(MyApplication.getContext(), RemotePatchManipulateImp(), RobustCallbackImp()).asyncRun()
    }

    private fun loadPatch() {
        AppPatchExecutor(MyApplication.getContext(), LocalPatchManipulateImp(), RobustCallbackImp()).run()
    }

    fun clearPatch() {
        FileUtils.delete(getPatchRootDir())
        clearAll()
    }

    fun updatePatchInfo(patchName: String?, patchHash: String?, appInit: Boolean) {
        setPatchName(patchName)
        setPatchHash(patchHash)
        setPatchAppInit(appInit)
    }
}