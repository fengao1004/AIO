package com.goldze.mvvmhabit.aioui.common.viewpagerfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIORvBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.listener.AIOViewPagerOnTabSelectedListener
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerFragmentModel
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.http.impl.MeditationRepository
import com.goldze.mvvmhabit.databinding.FragmentViewpagerAioBinding
import com.google.android.material.tabs.TabLayout
import me.goldze.mvvmhabit.base.BaseFragment

/**
 * 使用这个要自定义一下 recyclerview 的 item Layout
 * HttpRepository 请求数据，详情查看继承的子类
 * 观察 viewModel.itemClickEvent 设置子项点击事件
 * 创建 AIOViewPagerBindingAdapter 时传入 AIORvBindingAdapter子类类型来指定 rv item 如何绑定
 * tabs 请求类型
 * 例子参考子类
 */
abstract class AIOViewPagerFragment : BaseFragment<FragmentViewpagerAioBinding, AIOViewPagerFragmentModel>() {

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

        // viewpager tl 关联
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(AIOViewPagerOnTabSelectedListener(binding.viewPager))
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.tabLoadComplete.observe(this) {
            for (itemDatum in viewModel.tabItemData) {
                val tab = binding.tabs.newTab()
                val view = layoutInflater.inflate(R.layout.item_tablayout_topic, null)
                val tv = view.findViewById<TextView>(R.id.tvLabel)
                tv.text = itemDatum.name
                tab.customView = view
                binding.tabs.addTab(tab)

                val viewpagerItemViewModel = AIOViewPagerItemViewModel(
                    viewModel,
                    activity!!.application,
                    getViewPagerRepository(),
                    getRvItemLayoutResId()
                )
                viewModel.items.add(viewpagerItemViewModel)
            }

            // 请求第一页数据
            if (viewModel.items.size > 0) {
                viewModel.items[0].onRefreshCommand.execute()
            }
        }

        viewModel.loadTabsData(getTabType())
    }

    abstract fun getTabType(): String

    abstract fun getRvItemLayoutResId(): Int

    abstract fun getViewPagerRepository(): ListRepository
}