package com.freebrio.robustdemo.patch

interface IRobustCallBack {
    /**
     * 获取补丁列表后，回调此方法
     * @param result Boolean
     * @param isNet Boolean
     * @param patches List<AppPatch>?
     */
    fun onPatchListFetched(result: Boolean, isNet: Boolean, patches: List<AppPatch>?)

    /**
     * 在获取补丁后，回调此方法
     * @param result Boolean
     * @param isNet Boolean
     * @param patch AppPatch?
     */
    fun onPatchFetched(result: Boolean, isNet: Boolean, patch: AppPatch?)

    /**
     * 在补丁应用后，回调此方法
     * @param result Boolean
     * @param patch AppPatch?
     */
    fun onPatchApplied(result: Boolean, patch: AppPatch?)

    fun logNotify(log: String?)

    fun exceptionNotify(throwable: Throwable?, where: String?)
}