package com.goldze.mvvmhabit.aioui.relax.train

import android.widget.TextView
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.webview.WebViewActivity
import me.goldze.mvvmhabit.utils.ToastUtils

class TrainFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("放松训练")
        binding.adapter?.spanCount = 1

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
                R.layout.item_rv_train
            )
            viewModel.items.add(viewpagerItemViewModel)

        }
    }

    override fun initViewObservable() {
        viewModel.itemClickEvent.observe(this) { entity ->
            ToastUtils.showLong(entity.toString())
        }
    }
}