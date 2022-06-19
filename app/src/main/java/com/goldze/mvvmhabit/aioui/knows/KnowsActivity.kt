package com.goldze.mvvmhabit.aioui.knows

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityKepuBinding
import com.goldze.mvvmhabit.databinding.ActivityKnowsBinding
import me.goldze.mvvmhabit.base.BaseActivity

class KnowsActivity : BaseActivity<ActivityKnowsBinding, KnowsModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_knows
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("心理知识")
    }
}