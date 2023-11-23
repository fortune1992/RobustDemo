package com.bikan.reading.patch

import com.xiaomi.bn.utils.base.KeepAll
import java.io.File

@KeepAll
data class AppPatch(
    var patchesInfoImplClassFullName: String? = null,
    var name: String? = null,
    var url: String? = null,
    var localPath: String? = null,
    var md5: String? = null,
    var appHash: String? = null,
    var isAppliedSuccess: Boolean = false
) {

    /**
     * 删除文件
     */
    fun delete(path: String) {
        val f = File(path)
        f.delete()
    }
}