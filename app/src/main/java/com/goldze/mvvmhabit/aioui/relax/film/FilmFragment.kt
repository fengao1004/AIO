package com.goldze.mvvmhabit.aioui.relax.film

import android.content.Intent
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.impl.VideoRepository
import com.goldze.mvvmhabit.aioui.video.VideoActivity
import com.goldze.mvvmhabit.aioui.video.bean.VideoBean
import com.goldze.mvvmhabit.utils.hide

class FilmFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("心理短片")
        binding.adapter?.spanCount = 3

        // 隐藏 tabs
        binding.tabs.hide()

        val viewpagerItemViewModel = AIOViewPagerItemViewModel(
            viewModel,
            activity!!.application,
            VideoRepository(),
            R.layout.item_rv_video
        )
        viewModel.items.add(viewpagerItemViewModel)

    }


    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            val videoBean = VideoBean(
                "http://cdn.xlxs.top/%5B%E9%AB%98%E6%B8%85%201080P%5D%20%E3%80%90%E5%A4%A9%E6%B0%94%E4%B9%8B%E5%AD%90%E3%80%91%E6%97%A0%E5%AD%97%E5%B9%95PV%E5%90%88%E9%9B%86%E3%80%901080P%E3%80%91_P2_%E6%98%A0%E7%94%BB%E3%80%8E%E5%A4%A9%E6%B0%97%E3%81%AE%E5%AD%90%E3%80%8F%E4%BA%88%E5%A0%B1%202.mp4",
                "天气之子",
                "天气之子",
                "[高清 1080P] 【天气之子】无字幕PV合...0P】_P2_映画『天気の子』予報 2",
                "1245"
            )
            val intent = Intent(context, VideoActivity::class.java).apply {
                putExtra("videoBean", videoBean)
            }
            startActivity(intent)
        }
    }
}