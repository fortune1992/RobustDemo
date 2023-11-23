package com.bikan.base.net

import com.bikan.base.utils.NetPreviewUtils

object Constants {
    const val CONNECT_TIMEOUT_MILLIS = 10
    const val READ_TIMEOUT_MILLIS = 10

    const val BLANK = "about:blank"
    const val COMMENT_APP_ID = "bikan"

    const val STATUS_CODE_SUCCESS = 200
    const val STATUS_CODE_ERROR = 500
    // 账号封禁，需要拍照申诉
    const val STATUS_CODE_ACCOUNT_FORBIDDEN_PHOTO = 2000
    // 账号封禁，需要点击申诉
    const val STATUS_CODE_ACCOUNT_FORBIDDEN_CLICK = 2001

    val FEED_HOST_SET = listOf(
            "feed.dev.browser.miui.com",
            "feed.browser.miui.com",
            "feed-test.browser.miui.com",
            "test.browser.miui.com")

    fun getHost(): String? {
        return if (NetPreviewUtils.isQAEnv()) {
            "https://feed-test.browser.miui.com"
        } else if (NetPreviewUtils.isNetPreview()) {
            "http://feed.dev.browser.miui.com"
        } else {
            "https://feed.browser.miui.com"
        }
    }
}