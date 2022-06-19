package com.goldze.mvvmhabit.aioui.kepu

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityKepuBinding
import me.goldze.mvvmhabit.base.BaseActivity

class KepuActivity : BaseActivity<ActivityKepuBinding,KepuViewModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_kepu
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("科普专区")
    }
}