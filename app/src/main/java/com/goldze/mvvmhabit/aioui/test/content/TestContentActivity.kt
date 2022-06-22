package com.goldze.mvvmhabit.aioui.test.content

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityTestContentBinding
import me.goldze.mvvmhabit.base.BaseActivity

class TestContentActivity : BaseActivity<ActivityTestContentBinding, TestContentModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_test_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("抑郁自评量表")
    }
}