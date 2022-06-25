package com.goldze.mvvmhabit.aioui.relax.gallery

import android.widget.TextView
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.webview.WebViewActivity

class GalleryFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("心理图库")
        binding.adapter?.spanCount = 2

        //模拟几个ViewPager页面
        for (i in 1..5) {
            val tab = binding.tabs.newTab()
            val view = layoutInflater.inflate(R.layout.item_tablayout_topic, null)
            val tv = view.findViewById<TextView>(R.id.tvLabel)
            tv.text = "Tab标题"
            tab.customView = view
            binding.tabs.addTab(tab)

            val viewpagerItemViewModel = AIOViewPagerItemViewModel(
                viewModel,
                activity!!.application,
                HttpRepository(),
                R.layout.item_rv_gallery
            )
            viewModel.items.add(viewpagerItemViewModel)

        }
    }



    override fun initViewObservable() {
        viewModel.itemClickEvent.observe(this) { entity ->
            WebViewActivity.start(
                viewModel.getApplication(),
                "心灵成长记",
                entity.detail,
                "1",
                true,
                WebViewActivity.MODE_SONIC
            )
        }
    }
}