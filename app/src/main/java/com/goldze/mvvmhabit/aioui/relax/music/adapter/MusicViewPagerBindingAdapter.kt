package com.goldze.mvvmhabit.aioui.relax.music.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicViewPagerItemViewModel
import com.goldze.mvvmhabit.databinding.ItemViewpagerMusicBinding
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter

class MusicViewPagerBindingAdapter :
    BindingViewPagerAdapter<MusicViewPagerItemViewModel>() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: MusicViewPagerItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
        //这里可以强转成 MusicViewPagerItemViewModel 对应的 ViewDataBinding
        val mBinding = binding as ItemViewpagerMusicBinding

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