package com.goldze.mvvmhabit.ui.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goldze.mvvmhabit.R

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 7:24 下午
 */
class BaseRootView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var baseView: View = LayoutInflater.from(context).inflate(R.layout.view_base_root, null, false)
    var logo: ImageView
    var appTitleView: TextView
    var pageTitleView: TextView
    var backIv: ImageView

    init {
        addView(
            baseView,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
        appTitleView = baseView.findViewById<TextView>(R.id.tv_app_title)
        pageTitleView = baseView.findViewById<TextView>(R.id.tv_page_title)
        logo = baseView.findViewById<ImageView>(R.id.iv_logo)
        backIv = baseView.findViewById<ImageView>(R.id.iv_back)
    }


    fun setTitleBackgroundColor(color: Int) {
        baseView.setBackgroundColor(color)
    }

    fun setAppTitle(appTitle: String) {
        appTitleView.text = appTitle
    }

    fun setPageTitle(pageTitle: String) {
        pageTitleView.text = pageTitle
    }

    fun setLogo(url: String) {
        //使用Glide框架加载图片
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.loading))
            .into(logo)
    }

    fun hidePageTitle() {
        pageTitleView.visibility = View.GONE
    }

}