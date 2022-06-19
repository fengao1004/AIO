package com.goldze.mvvmhabit.aioui.test

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityKnowsBinding
import com.goldze.mvvmhabit.databinding.ActivityTestBinding
import me.goldze.mvvmhabit.base.BaseActivity

class TestActivity : BaseActivity<ActivityTestBinding, TestModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_test
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("专业评测")
    }

}