package com.goldze.mvvmhabit.aioui.relax

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityKnowsBinding
import com.goldze.mvvmhabit.databinding.ActivityRelaxBinding
import me.goldze.mvvmhabit.base.BaseActivity

class RelaxActivity : BaseActivity<ActivityRelaxBinding, RelaxModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_relax
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("减压调适")
    }

}