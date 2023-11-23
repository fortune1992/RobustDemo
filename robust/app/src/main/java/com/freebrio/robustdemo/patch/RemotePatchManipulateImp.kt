package com.bikan.reading.patch

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.bikan.base.manager.NewsSchedulers
import com.bikan.base.model.ModeBase
import com.bikan.reading.model.PatchInfo
import com.bikan.reading.net.RetrofitServiceFactory
import com.bikan.reading.net.UploadLogProxy
import com.meituan.robust.RobustApkHashUtils
import com.xiaomi.bn.utils.coreutils.CloseUtils
import com.xiaomi.bn.utils.coreutils.DeviceUtils
import com.xiaomi.bn.utils.coreutils.FileUtils
import com.xiaomi.bn.utils.coreutils.GsonUtils
import com.xiaomi.bn.utils.coreutils.ZipUtils
import com.xiaomi.bn.utils.encrypt.AESUtils
import com.xiaomi.bn.utils.encrypt.MD5Utils
import io.reactivex.functions.Consumer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.util.ArrayList

const val WRITE_FILE_LOCK = "patch_w.lock"
const val KEY_PATCH_PREFIX = "android_patch_"
const val AES_KEY = "6666664aabf2ce8756cb31971a227b17"

class RemotePatchManipulateImp : AbsPatchManipulate() {
    /***
     *
     * 联网获取最新的补丁
     * @param context
     *
     * @return
     */
    @SuppressLint("CheckResult")
    override fun fetchPatchList(context: Context, consumer: AppPatchExecutor.FetchResultConsumer) {
        RetrofitServiceFactory.getCommonService().getPatchInfo(KEY_PATCH_PREFIX + DeviceUtils.getAppVersionCode() + "_" + RobustApkHashUtils.readRobustApkHash(context))
            .doOnNext {
                if (it.body() == null) {
                    PatchManager.clearPatch()
                }
            }
            .observeOn(NewsSchedulers.IO)
            .map<ModeBase<String>> { it.body() }
            .map<PatchInfo> {
                if (it.data.isNullOrEmpty()) {
                    PatchInfo(disablePatch = true)
                } else {
                    GsonUtils.fromJson(it.data, PatchInfo::class.java)
                }
            }
            .doOnNext {
                if (it == null || it.disablePatch) {
                    PatchManager.clearPatch()
                } else {
                    it.hash = AESUtils.decrypt(it.hash, AES_KEY)
                    val samePatch = checkSamePatch(it)
                    if (!samePatch) {
                        PatchManager.clearPatch()
                        downloadApkPatch(it, consumer)
                    }
                }
            }
            .subscribe(Consumer<PatchInfo> { },
                Consumer { UploadLogProxy.uploadException(it, UploadLogProxy.EXCEPTION_MODULE_TRY_CATCH, "fetchPatch") })
    }

    override fun verifyPatch(context: Context, patch: AppPatch): Boolean {
        // do your verification, put the real patch to patch
        // 放到app的私有目录
        patch.localPath = getPatchFilePath()
        return true
    }

    /**
     * @param patch
     * @return you may download your patches here, you can check whether patch is in the phone
     */
    override fun ensurePatchExist(patch: AppPatch): Boolean {
        return File(patch.localPath).exists()
    }

    private fun checkSamePatch(patchInfo: PatchInfo): Boolean {
        return patchInfo.hash?.equals(getPatchHash()) == true && File(getPatchFilePath()).exists()
    }

    private fun downloadApkPatch(patchInfo: PatchInfo, consumer: AppPatchExecutor.FetchResultConsumer) {
        if (TextUtils.isEmpty(patchInfo.url) || TextUtils.isEmpty(patchInfo.hash)) {
            return
        }
        var channle: FileChannel? = null
        var lock: FileLock? = null
        try {
            channle = FileOutputStream(getPatchRootDir() + WRITE_FILE_LOCK).channel
            lock = channle.lock()

            val patchFile = File(getPatchRootDir(), "patch")
            if (patchFile.exists()) {
                patchFile.delete()
            }

            downloadFileSync(patchInfo.url!!, patchFile)
            try {
                if (MD5Utils.getFileMD5(patchFile) == patchInfo.hash) {
                    PatchManager.updatePatchInfo(patchInfo.hash, patchInfo.hash, patchInfo.appInit)
                    // 解压缩
                    ZipUtils.unZipFile(patchFile.path, getPatchDirName())
                    val patch = AppPatch()
                    patch.md5 = patchInfo.hash
                    patch.name = getPatchName()
                    patch.localPath = getPatchFilePath()
                    patch.patchesInfoImplClassFullName = "$PATCH_INFO_CLASS_NAME_PREFIX.PatchesInfoImpl"
                    val patches = ArrayList<AppPatch>()
                    patches.add(patch)
                    consumer.accept(patches)
                }
                FileUtils.deleteFile(patchFile)
            } catch (e: IOException) {
                e.printStackTrace()
                setPatchHash(null)
                setPatchName(null)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            lock?.release()
            CloseUtils.closeIO(channle)
        }
    }
}
