package com.freebrio.robustdemo.network

object Constants {
    const val CONNECT_TIMEOUT_MILLIS = 10
    const val READ_TIMEOUT_MILLIS = 10

    const val BLANK = "about:blank"
    const val COMMENT_APP_ID = "bikan"

    const val STATUS_CODE_SUCCESS = 200
    const val STATUS_CODE_ERROR = 500

    fun getHost(): String {
        return "https://feed.browser.miui.com"
    }
}