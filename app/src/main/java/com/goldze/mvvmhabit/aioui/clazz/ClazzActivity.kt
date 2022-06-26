package com.goldze.mvvmhabit.aioui.clazz

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.clazz.adapter.ClazzViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.relax.music.adapter.MusicViewPagerBindingAdapter
import com.goldze.mvvmhabit.databinding.ActivityClazzBinding
import com.google.android.material.tabs.TabLayout
import me.goldze.mvvmhabit.base.BaseActivity

class ClazzActivity : BaseActivity<ActivityClazzBinding, ClazzViewModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_clazz
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("心理课堂")
        viewModel.loadData()
        binding.tabs.setupWithViewPager(binding.viewPager)
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.adapter = ClazzViewPagerBindingAdapter()
    }
}