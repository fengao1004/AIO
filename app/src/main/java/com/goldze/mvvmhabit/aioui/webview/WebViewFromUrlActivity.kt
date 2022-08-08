package com.goldze.mvvmhabit.aioui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebSettings.ZoomDensity
import android.webkit.WebViewClient
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityWebviewUrlBinding
import me.goldze.mvvmhabit.base.BaseActivity


class WebViewFromUrlActivity : BaseActivity<ActivityWebviewUrlBinding, WebViewFromUrlModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_webview_url
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        var title = intent.getStringExtra("title")
        var url = intent.getStringExtra("url")
        Log.i("fengao_xiaomi", "initData: $url")
        binding.brRootView.setPageTitle("报告")
        loadUrl(url!!)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadUrl(url: String) {
        val webView = binding.webview
        val settings: WebSettings = webView.settings
        settings.textZoom = 100;
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }

}