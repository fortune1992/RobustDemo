package com.bikan.reading.model

import com.google.gson.annotations.SerializedName

data class PatchInfo(
    @SerializedName("url") var url: String? = null,

    @SerializedName("hash") var hash: String? = null,

    @SerializedName("appInit") var appInit: Boolean = false,

    @SerializedName("disablePatch") var disablePatch: Boolean = false
)