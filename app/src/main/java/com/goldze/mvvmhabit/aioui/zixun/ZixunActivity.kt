package com.goldze.mvvmhabit.aioui.zixun

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityKnowsBinding
import com.goldze.mvvmhabit.databinding.ActivityZixunBinding
import me.goldze.mvvmhabit.base.BaseActivity

class ZixunActivity : BaseActivity<ActivityZixunBinding, ZixunModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_zixun
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("咨询服务")
    }

}