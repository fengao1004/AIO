package com.goldze.mvvmhabit.aioui.relax.film

import android.content.Intent
import android.text.TextUtils
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
                    entity.resourcesUrl ?: "",
                    entity.name ?: "",
                    entity.name ?: "",
                    entity.filmDescribe ?: "",
                    "点击数：${entity.clickCount.toString()}"
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