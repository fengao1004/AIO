package com.goldze.mvvmhabit.aioui.gonggao

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityClazzBinding
import com.goldze.mvvmhabit.databinding.ActivityGonggaoBinding
import me.goldze.mvvmhabit.base.BaseActivity

class GonggaoActivity : BaseActivity<ActivityGonggaoBinding, GonggaoViewModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_gonggao
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("系统公告")
        viewModel.loadList()
    }
}