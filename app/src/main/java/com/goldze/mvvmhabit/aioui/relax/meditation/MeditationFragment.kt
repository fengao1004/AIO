package com.goldze.mvvmhabit.aioui.relax.meditation

import android.content.Intent
import android.text.TextUtils
import android.widget.TextView
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.list.MeditationRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.http.impl.MeditationRepository
import com.goldze.mvvmhabit.aioui.video.VideoActivity
import com.goldze.mvvmhabit.aioui.video.bean.VideoBean

class MeditationFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        // 给ViewPager设置adapter
        binding.adapter = AIOViewPagerBindingAdapter(adapterClazz = MeditationRvBindingAdapter::class.java)

        binding.brRootView.setPageTitle("放松训练")
        binding.adapter?.spanCount = 1
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            if (entity is MeditationRecord) {
                val videoBean = VideoBean(
                    entity.resourcesUrl ?: "",
                    entity.name ?: "",
                    entity.name ?: "",
                    entity.meditationDescribe ?: "",
                    "点击数：${entity.clickCount.toString()}"
                )
                val intent = Intent(context, VideoActivity::class.java).apply {
                    putExtra("videoBean", videoBean)
                }
                startActivity(intent)
            }
        }
    }

    override fun getTabType(): String {
        return "meditation"
    }

    override fun getRvItemLayoutResId(): Int {
        return R.layout.item_rv_meditation
    }

    override fun getViewPagerRepository(): ListRepository {
        return MeditationRepository()
    }
}