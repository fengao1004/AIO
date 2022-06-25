package com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.databinding.ItemViewpagerAioBinding
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter

/**
 * @param spanCount recyclerView 默认是一列的网格布局，如果需要多列，可以指定 spanCount
 */
class AIOViewPagerBindingAdapter(var spanCount: Int = 1) : BindingViewPagerAdapter<AIOViewPagerItemViewModel>() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: AIOViewPagerItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
        //这里可以强转成 AIOViewPagerItemViewModel 对应的 ViewDataBinding
        val mBinding = binding as ItemViewpagerAioBinding

        val layoutManager = mBinding.recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanCount = spanCount
        }

        // 销毁又重建时，binding 虽然更新了，但 viewModel 还是旧的，所以要移除之前的 observer，多个不起作用
            mBinding.viewModel!!.uiChangeObservable.finishRefreshing.removeObservers(mBinding.lifecycleOwner!!)
        mBinding.viewModel!!.uiChangeObservable.finishRefreshing.observe(mBinding.lifecycleOwner!!) {
            mBinding.twinklingRefreshLayout.finishRefreshing()
            mBinding.twinklingRefreshLayout.finishLoadmore()
        }

        mBinding.viewModel!!.uiChangeObservable.finishLoadmore.removeObservers(mBinding.lifecycleOwner!!)
        mBinding.viewModel!!.uiChangeObservable.finishLoadmore.observe(mBinding.lifecycleOwner!!) {
            // 结束刷新
            mBinding.twinklingRefreshLayout.finishRefreshing()
            mBinding.twinklingRefreshLayout.finishLoadmore()
        }

        // 没有旧数据就请求数据
        if (mBinding.viewModel!!.observableList.isEmpty()) {
            mBinding.twinklingRefreshLayout.startRefresh()
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

}