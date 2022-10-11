package com.goldze.mvvmhabit.aioui.webview

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/10/9
 * Time: 4:59 下午
 */
class MyWebChromeClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return if (url?.startsWith("aio://") == true) {

            true
        } else {
            super.shouldOverrideUrlLoading(view, url)
        }
    }
}