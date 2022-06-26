package com.goldze.mvvmhabit.aioui.clazz.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.clazz.vm.ClazzViewPagerItemViewModel
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter
import com.goldze.mvvmhabit.databinding.ItemViewpagerClazzBinding

class ClazzViewPagerBindingAdapter :
    BindingViewPagerAdapter<ClazzViewPagerItemViewModel>() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: ClazzViewPagerItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
        val mBinding = binding as ItemViewpagerClazzBinding

        // 销毁又重建时，binding 虽然更新了，但 viewModel 还是旧的，所以要移除之前的 observer，多个不起作用
        mBinding.viewModel!!.uiChangeObservable.finishRefreshing.removeObservers(mBinding.lifecycleOwner!!)
        mBinding.viewModel!!.uiChangeObservable.finishRefreshing.observe(mBinding.lifecycleOwner!!) {
            Log.i("fengao_xiaomi", "onBindBinding:1 ")
            mBinding.twinklingRefreshLayout.finishRefreshing()
            mBinding.twinklingRefreshLayout.finishLoadmore()
        }

        mBinding.viewModel!!.uiChangeObservable.finishLoadmore.removeObservers(mBinding.lifecycleOwner!!)
        mBinding.viewModel!!.uiChangeObservable.finishLoadmore.observe(mBinding.lifecycleOwner!!) {
            // 结束刷新
            Log.i("fengao_xiaomi", "onBindBinding:2 ")
            mBinding.twinklingRefreshLayout.finishRefreshing()
            mBinding.twinklingRefreshLayout.finishLoadmore()
        }

        // 没有旧数据就请求数据
        if (mBinding.viewModel!!.observableList.isEmpty()) {
            Log.i("fengao_xiaomi", "onBindBinding:3 ")
            mBinding.twinklingRefreshLayout.startRefresh()
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

}