package com.goldze.mvvmhabit.aioui.relax.film

import android.content.Intent
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.list.FilmRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.http.impl.FilmRepository
import com.goldze.mvvmhabit.aioui.video.VideoActivity
import com.goldze.mvvmhabit.aioui.video.bean.VideoBean
import com.goldze.mvvmhabit.utils.hide

class FilmFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()

        //给ViewPager设置adapter
        binding.adapter = AIOViewPagerBindingAdapter(adapterClazz = FilmRvBindingAdapter::class.java)

        binding.brRootView.setPageTitle("心理短片")
        binding.adapter?.spanCount = 3

        // 隐藏 tabs
        binding.tabs.hide()
    }


    override fun initViewObservable() {
        viewModel.itemClickEvent.observe(this) { entity ->
            if (entity is FilmRecord) {
                val videoBean = VideoBean(
                    entity.resourcesUrl
                        ?: "http://cdn.xlxs.top/%E7%94%B7%E4%B8%BB%E4%BE%A7%E9%9D%A2%E5%9D%90%E5%9C%A8%E6%B5%B7%E8%BE%B9%EF%BC%8C%E5%A5%B3%E4%B8%BB%E8%83%8C%E9%9D%A2%E5%9D%90%E5%9C%A8%E6%B5%B7%E8%BE%B9.mp4",
                    entity.name ?: "",
                    entity.name ?: "",
                    entity.filmDescribe ?: "",
                    entity.clickCount.toString()
                )
                val intent = Intent(context, VideoActivity::class.java).apply {
                    putExtra("videoBean", videoBean)
                }
                startActivity(intent)
            }
        }

        // 默认只有一个 tab
        val viewpagerItemViewModel = AIOViewPagerItemViewModel(
            viewModel,
            activity!!.application,
            getViewPagerRepository(),
            getRvItemLayoutResId()
        )
        viewModel.items.add(viewpagerItemViewModel)
        viewpagerItemViewModel.onRefreshCommand.execute()

    }

    override fun getTabType(): String {
        return ""
    }

    override fun getRvItemLayoutResId(): Int {
        return R.layout.item_rv_film
    }

    override fun getViewPagerRepository(): ListRepository {
        return FilmRepository()
    }
}