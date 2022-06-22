package com.goldze.mvvmhabit.aioui.relax.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.relax.music.adapter.MusicViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicModel
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicViewPagerItemViewModel
import com.goldze.mvvmhabit.app.Injection
import com.goldze.mvvmhabit.databinding.FragmentMusicBinding
import com.goldze.mvvmhabit.ui.main.DemoActivity
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import me.goldze.mvvmhabit.base.BaseFragment
import me.goldze.mvvmhabit.utils.ToastUtils

class MusicFragment : BaseFragment<FragmentMusicBinding, MusicModel>() {


    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_music
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }


    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("音乐放松")
        binding.musicIcon.setOnClickListener {
            startActivity(DemoActivity::class.java)
        }

        // 使用 TabLayout 和 ViewPager 相关联
        binding.tabs.setupWithViewPager(binding.viewPager)
        binding.viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.tabs))

        //给ViewPager设置adapter
        binding.adapter = MusicViewPagerBindingAdapter()

    }

    override fun initViewObservable() {
        viewModel.itemClickEvent.observe(this) { text -> ToastUtils.showShort("position：$text") }

        //模拟3个ViewPager页面
        for (i in 1..5) {
            val viewpagerItemViewModel = MusicViewPagerItemViewModel(
                viewModel,
                activity!!.application,
                Injection.provideDemoRepository()
            )
            viewModel.items.add(viewpagerItemViewModel)
        }
    }

}