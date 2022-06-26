package com.goldze.mvvmhabit.aioui.common.viewpagerfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.listener.AIOViewPagerOnTabSelectedListener
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerFragmentModel
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.webview.WebViewActivity
import com.goldze.mvvmhabit.aioui.webview.WebViewActivity.Companion.start
import com.goldze.mvvmhabit.databinding.FragmentViewpagerAioBinding
import com.google.android.material.tabs.TabLayout
import me.goldze.mvvmhabit.base.BaseFragment
import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * 使用这个要自定义一下 recyclerview 的 item Layout
 * 还有 httpRepository 请求数据，详情查看继承的子类
 * 以及在 initViewObservable 中观察 viewModel.itemClickEvent 设置子项点击事件
 *
 * 例子参考子类
 */
open class AIOViewPagerFragment : BaseFragment<FragmentViewpagerAioBinding, AIOViewPagerFragmentModel>() {

    val TAG = "AIOViewPagerFragment"

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_viewpager_aio
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()

        binding.brRootView.setAppTitle("心理自助服务一体机")
        binding.brRootView.setPageTitle("AIO 页面")
        binding.brRootView.backIv.setOnClickListener {
            activity?.finish()
        }

        //给ViewPager设置adapter
        binding.adapter = AIOViewPagerBindingAdapter()
        // viewpager tl 关联
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(AIOViewPagerOnTabSelectedListener(binding.viewPager))
    }

    override fun initViewObservable() {
        super.initViewObservable()
        // 请求第一页数据
        if (viewModel.items.size > 0) {
            viewModel.items[0].onRefreshCommand.execute()
        }
    }
}