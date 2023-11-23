package com.bikan.reading.patch

import com.xiaomi.bn.utils.coreutils.ApplicationStatus
import com.xiaomi.bn.utils.coreutils.FileUtils
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
        AppPatchExecutor(ApplicationStatus.getApplicationContext(), RemotePatchManipulateImp(), RobustCallbackImp()).asyncRun()
    }

    private fun loadPatch() {
        AppPatchExecutor(ApplicationStatus.getApplicationContext(), LocalPatchManipulateImp(), RobustCallbackImp()).run()
    }

    fun clearPatch() {
        FileUtils.deleteDir(getPatchRootDir())
        clearAll()
    }

    fun updatePatchInfo(patchName: String?, patchHash: String?, appInit: Boolean) {
        setPatchName(patchName)
        setPatchHash(patchHash)
        setPatchAppInit(appInit)
    }
}