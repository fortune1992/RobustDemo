package com.freebrio.robustdemo.patch

import android.content.Context
import android.text.TextUtils
import androidx.core.util.Consumer
import com.meituan.robust.ChangeQuickRedirect
import com.meituan.robust.PatchesInfo
import dalvik.system.DexClassLoader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.lang.reflect.Field
import java.util.concurrent.atomic.AtomicInteger

private val REQUEST_PATCH_COUNT: AtomicInteger = AtomicInteger(0)

private const val ROBUST_PATCH_CACHE_DIR = "patch_cache"

class AppPatchExecutor(context: Context, private var patchManipulate: AbsPatchManipulate, private var robustCallBack: IRobustCallBack) {
    private var context: Context = context.applicationContext

    fun run() {
        try {
            robustCallBack.logNotify("begin patch, patch count: ${REQUEST_PATCH_COUNT.get()}")
            if (REQUEST_PATCH_COUNT.incrementAndGet() > 5) {
                return
            }
            // 拉取补丁列表
            fetchPatchList(FetchResultConsumer())
        } catch (t: Throwable) {
            robustCallBack.exceptionNotify(t, "method:run")
        }
    }

    fun asyncRun() {
        GlobalScope.launch {
            run()
        }
    }

    /**
     * 拉取补丁列表
     */
    private fun fetchPatchList(consumer: FetchResultConsumer) {
        patchManipulate.fetchPatchList(context, consumer)
    }

    /**
     * 应用补丁列表
     */
    private fun applyPatchList(patches: List<AppPatch>?) {
        if (null == patches || patches.isEmpty()) {
            return
        }
        robustCallBack.onPatchListFetched(result = true, isNet = (patchManipulate is RemotePatchManipulateImp),
            patches = patches)
        robustCallBack.logNotify("apply patch, patchManipulate list size is ${patches.size}")
        for (p in patches) {
            if (p.isAppliedSuccess) {
                robustCallBack.logNotify("p.isAppliedSuccess() skip ${p.localPath}")
                continue
            }
            if (patchManipulate.ensurePatchExist(p)) {
                var currentPatchResult = false
                try {
                    currentPatchResult = patch(context, p)
                } catch (t: Throwable) {
                    robustCallBack.exceptionNotify(t, "method:applyPatchList")
                }

                if (currentPatchResult) {
                    // 设置patch 状态为成功
                    p.isAppliedSuccess = true
                    // 统计PATCH成功率 PATCH成功
                    robustCallBack.onPatchApplied(true, p)
                } else {
                    // 统计PATCH成功率 PATCH失败
                    robustCallBack.onPatchApplied(false, p)
                }
                robustCallBack.logNotify("patch LocalPath: ${p.localPath}, apply result: $currentPatchResult")
            }
        }
    }

    private fun patch(context: Context, patch: AppPatch): Boolean {
        if (!patchManipulate.verifyPatch(context, patch)) {
            robustCallBack.logNotify("verifyPatch failure, patch info:" + "id = ${patch.name}, md5 = ${patch.md5}")
            return false
        }

        var classLoader: ClassLoader? = null

        try {
            val dexOutputDir = getPatchCacheDirPath(context, patch.name + patch.md5)
            classLoader = DexClassLoader(patch.localPath, dexOutputDir.absolutePath, null, AppPatchExecutor::class.java.classLoader)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

        if (null == classLoader) {
            return false
        }

        var patchClass: Class<*>
        var sourceClass: Class<*>

        val patchesInfoClass: Class<*>
        var patchesInfo: PatchesInfo? = null
        try {
            patchesInfoClass = classLoader.loadClass(patch.patchesInfoImplClassFullName)
            patchesInfo = patchesInfoClass.newInstance() as PatchesInfo
        } catch (t: Throwable) {
            robustCallBack.exceptionNotify(t, "method:patch")
        }

        if (patchesInfo == null) {
            robustCallBack.logNotify("patchesInfo is null, patch info:" + "id = ${patch.name}, md5 = ${patch.md5}")
            return false
        }

        // classes need to patch
        val patchedClasses = patchesInfo.patchedClassesInfo
        if (null == patchedClasses || patchedClasses.isEmpty()) {
            // 手写的补丁有时候会返回一个空list
            return true
        }

        var isClassNotFoundException = false
        for (patchedClassInfo in patchedClasses) {
            val patchedClassName = patchedClassInfo.patchedClassName
            val patchClassName = patchedClassInfo.patchClassName
            if (TextUtils.isEmpty(patchedClassName) || TextUtils.isEmpty(patchClassName)) {
                robustCallBack.logNotify("patchedClasses or patchClassName is empty, patch info:" + "id = ${patch.name}, md5 = ${patch.md5}")
                continue
            }
            robustCallBack.logNotify("current path:$patchedClassName")
            try {
                try {
                    sourceClass = classLoader.loadClass(patchedClassName.trim { it <= ' ' })
                } catch (e: ClassNotFoundException) {
                    isClassNotFoundException = true
                    continue
                }

                val fields = sourceClass.declaredFields
                var changeQuickRedirectField: Field? = null
                for (field in fields) {
                    if (TextUtils.equals(field.type.canonicalName, ChangeQuickRedirect::class.java.canonicalName) && TextUtils.equals(field.declaringClass.canonicalName, sourceClass.canonicalName)) {
                        changeQuickRedirectField = field
                        break
                    }
                }
                if (changeQuickRedirectField == null) {
                    robustCallBack.logNotify("changeQuickRedirectField  is null, patch info:" + "id = ${patch.name}, md5 = ${patch.md5}")
                    continue
                }
                robustCallBack.logNotify("current path:$patchedClassName find:ChangeQuickRedirect $patchClassName")
                try {
                    patchClass = classLoader.loadClass(patchClassName)
                    val patchObject = patchClass.newInstance()
                    changeQuickRedirectField.isAccessible = true
                    changeQuickRedirectField.set(null, patchObject)
                    robustCallBack.logNotify("changeQuickRedirectField set success $patchClassName")
                } catch (t: Throwable) {
                    robustCallBack.exceptionNotify(t, "method:patch")
                }
            } catch (t: Throwable) {
                robustCallBack.exceptionNotify(t, "method:patch")
            }
        }
        robustCallBack.logNotify("patch finished")
        return !isClassNotFoundException
    }

    private fun getPatchCacheDirPath(c: Context, key: String): File {
        val patchTempDir = c.getDir(ROBUST_PATCH_CACHE_DIR + key, Context.MODE_PRIVATE)
        if (!patchTempDir.exists()) {
            patchTempDir.mkdir()
        }

        return patchTempDir
    }

    inner class FetchResultConsumer : Consumer<List<AppPatch>?> {
        override fun accept(t: List<AppPatch>?) {
            applyPatchList(t)
        }
    }
}
