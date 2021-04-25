package com.freebrio.robustdemo

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.meituan.robust.Patch
import com.meituan.robust.PatchExecutor
import com.meituan.robust.RobustCallBack
import com.meituan.robust.patch.annotaion.Modify
import com.tbruyelle.rxpermissions3.RxPermissions
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.content_tv)

        val file =
            File(externalCacheDir?.absolutePath + File.separator + "robust" + File.separator + "patch ")
        if (!file.exists()) {
            file.mkdirs()
        }
        RxPermissions(this)
            .request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { granted ->
            }


    }

    fun getString(): String {
        return "111"
    }

    fun loadPatch(view: View) {
        PatchExecutor(this, PatchManipulateImp(), object : RobustCallBack {
            override fun onPatchListFetched(
                result: Boolean,
                isNet: Boolean,
                patches: MutableList<Patch>?
            ) {
            }

            override fun onPatchFetched(result: Boolean, isNet: Boolean, patch: Patch?) {
                Log.d("swt", "onPatchFetched")
            }

            override fun onPatchApplied(result: Boolean, patch: Patch?) {
                Log.d("swt", "onPatchApplied")

            }

            override fun logNotify(log: String?, where: String?) {
                Log.d("swt", "logNotify")

            }

            override fun exceptionNotify(throwable: Throwable?, where: String?) {
                Log.d("swt", "exceptionNotify")

            }

        }).start()
    }

    @Modify
    fun setTV(view: View) {
        textView.text = getString1()
    }

    fun getString1(): String {
        return "222"
    }
}