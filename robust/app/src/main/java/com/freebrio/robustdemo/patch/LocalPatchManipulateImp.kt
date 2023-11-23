package com.freebrio.robustdemo.patch

import android.content.Context
import java.io.File
import java.util.ArrayList

class LocalPatchManipulateImp : AbsPatchManipulate() {

    override fun fetchPatchList(context: Context, consumer: AppPatchExecutor.FetchResultConsumer) {
        val patch = AppPatch()
        patch.name = getPatchName()
        patch.md5 = getPatchHash()
        // 放到app的私有目录
        patch.localPath = getPatchFilePath()
        // setPatchesInfoImplClassFullName 设置项各个App可以独立定制，需要确保的是setPatchesInfoImplClassFullName设置的包名是和xml配置项patchPackname保持一致，而且类名必须是：PatchesInfoImpl
        // 请注意这里的设置
        patch.patchesInfoImplClassFullName = "$PATCH_INFO_CLASS_NAME_PREFIX.PatchesInfoImpl"
        val patches = ArrayList<AppPatch>()
        patches.add(patch)
        consumer.accept(patches)
    }

    override fun verifyPatch(context: Context, patch: AppPatch): Boolean {
        return true
    }

    override fun ensurePatchExist(patch: AppPatch): Boolean {
        return File(patch.localPath).exists()
    }
}
