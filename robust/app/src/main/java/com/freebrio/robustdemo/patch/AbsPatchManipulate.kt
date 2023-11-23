package com.freebrio.robustdemo.patch

import android.content.Context

abstract class AbsPatchManipulate {
    /**
     * 获取补丁列表
     * @param context Context
     * @param consumer AppPatchExecutor.FetchResultConsumer
     */
    abstract fun fetchPatchList(context: Context, consumer: AppPatchExecutor.FetchResultConsumer)

    /**
     * 验证补丁文件
     *
     * @param context
     * @param patch
     * @return 校验结果
     */
    abstract fun verifyPatch(context: Context, patch: AppPatch): Boolean

    /**
     * 努力确保补丁文件存在
     *
     * @param patch
     * @return 是否存在
     */
    abstract fun ensurePatchExist(patch: AppPatch): Boolean
}