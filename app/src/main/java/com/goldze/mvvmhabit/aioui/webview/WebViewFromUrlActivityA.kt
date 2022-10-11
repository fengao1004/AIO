package com.goldze.mvvmhabit.aioui.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.webview.bean.TuijianBeanData
import com.goldze.mvvmhabit.databinding.ActivityWebviewUrlABinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseActivity
import java.lang.Boolean
import java.lang.reflect.Method
import kotlin.Exception
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.stackTraceToString


class WebViewFromUrlActivityA : BaseActivity<ActivityWebviewUrlABinding, WebViewFromUrlModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_webview_url_a
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    var list: MutableList<TuijianBeanData> = arrayListOf()

    var id: Long = 0L
    override fun initData() {
        super.initData()
        var title = intent.getStringExtra("title")
        var url = intent.getStringExtra("url")
        id = intent.getLongExtra("id", 0L)
        Log.i("fengao_xiaomi", "initData: $url")
        binding.brRootView.setPageTitle(title)
        loadUrl(url!!)
    }

    @SuppressLint("SetJavaScriptEnabled", "CheckResult")
    private fun loadUrl(url: String) {
        val webView = binding.webview
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true; // 启用javascript
        settings.domStorageEnabled = true; // 支持HTML5中的一些控件标签
        settings.builtInZoomControls = false; // 自选，非必要 作者：永远的祈妹 https://www.bilibili.com/read/cv16642400 出处：bilibili
        settings.textZoom = 100;
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                val method: Method? = Class.forName("android.webkit.WebView")
                    .getMethod("setWebContentsDebuggingEnabled", Boolean.TYPE)
                if (method != null) {
                    method.isAccessible = true
                    method.invoke(null, true)
                }
            } catch (e: Exception) {
                // do nothing
            }
        }
        webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress >99){
                    binding.rcTuijian.visibility = View.VISIBLE
                    binding.llTuijian.visibility = View.VISIBLE
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): kotlin.Boolean {
                if (url.startsWith("http://") || url.startsWith("https://")) { // 4.0以上必须要加
                    view.loadUrl(url)
                    return true
                }
                return false
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError
            ) {
                //super.onReceivedSslError(view, handler, error)
                when (error.primaryError) {
                    SslError.SSL_INVALID, SslError.SSL_UNTRUSTED -> {
                        handler.proceed()
                        handler.cancel()
                    }
                    else -> handler.cancel()
                }
            }
        }
        webView.loadUrl(url)
        viewModel.model.api.tuijian(2, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    list.clear()
                    list.addAll(it.data)
                    Log.i("fengao_xiaomi", "loadUrl: ${list.size}")
                    var adapter = TuijianAdapter(this, this, list)
                    binding.rcTuijian.layoutManager = LinearLayoutManager(this)
                    binding.rcTuijian.adapter = adapter
                }
            }, {
                it.printStackTrace()
                Log.i("fengao_xiaomi", "loadUrl: " + it.stackTraceToString())
            })
    }

}

class TuijianAdapter(
    var activity: Activity,
    var context: Context,
    var list: List<TuijianBeanData>
) :
    RecyclerView.Adapter<TuijianViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TuijianViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_tuijian, parent, false)
        return TuijianViewHolder(activity, view)
    }

    override fun onBindViewHolder(holder: TuijianViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class TuijianViewHolder(var activity: Activity, view: View) : RecyclerView.ViewHolder(view) {
    var iv_img = view.findViewById<ImageView>(R.id.iv_img)
    var tv_name = view.findViewById<TextView>(R.id.tv_name)
    var tv_num = view.findViewById<TextView>(R.id.tv_num)
    fun onBind(bean: TuijianBeanData) {
        tv_name.text = bean.name
        tv_num.text = "点击量" + bean.clickCount
        Glide.with(itemView.context)
            .load(bean.faceImage)
            .apply(RequestOptions().placeholder(R.drawable.loading))
            .into(iv_img)
        itemView.setOnClickListener {
            Util.jump(bean.moduleCode, bean.id.toString(), activity, bean.name)
        }
    }

}