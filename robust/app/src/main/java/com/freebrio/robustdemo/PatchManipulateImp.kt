package com.freebrio.robustdemo

import android.content.Context
import android.os.Environment
import com.meituan.robust.Patch
import com.meituan.robust.PatchManipulate
import java.io.File


/**
 * introduce：here is introduce
 * author：sunwentao
 * email：wentao.sun@freebrio.com
 * data: 2021/04/23
 */
class PatchManipulateImp : PatchManipulate() {
    override fun fetchPatchList(context: Context): MutableList<Patch> {
        val patch = Patch()
        patch.name = "test"
        patch.tempPath =
            context.externalCacheDir?.absolutePath + File.separator + "robust" + File.separator + "patch"
        patch.localPath =
            context.externalCacheDir?.absolutePath + File.separator + "robust" + File.separator + "patch"
        patch.patchesInfoImplClassFullName = "com.freebrio.robustdemo.PatchesInfoImpl"
        val list = arrayListOf<Patch>()
        list.add(patch)
        return list
    }

    override fun verifyPatch(context: Context?, patch: Patch?): Boolean {
        return true
    }

    override fun ensurePatchExist(patch: Patch?): Boolean {
        return true
    }
}