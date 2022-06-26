package com.goldze.mvvmhabit.aioui.clazz.content

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityScanBinding
import me.goldze.mvvmhabit.base.BaseActivity

class ClazzContentActivity : BaseActivity<ActivityScanBinding, ClazzContentModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_scan
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("情绪扫描")
    }
}