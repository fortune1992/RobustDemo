package com.freebrio.robustdemo.model

import com.freebrio.robustdemo.annotation.KeepAll
import com.google.gson.annotations.SerializedName

@KeepAll
data class PatchInfo(
    @SerializedName("url") var url: String? = null,

    @SerializedName("hash") var hash: String? = null,

    @SerializedName("appInit") var appInit: Boolean = false,

    @SerializedName("disablePatch") var disablePatch: Boolean = false
)