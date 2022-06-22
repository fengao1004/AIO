package com.goldze.mvvmhabit.aioui.video

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityScanBinding
import com.goldze.mvvmhabit.databinding.ActivityVideoBinding
import me.goldze.mvvmhabit.base.BaseActivity

class VideoActivity : BaseActivity<ActivityVideoBinding, VideoModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_video
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("情绪扫描")
    }
}