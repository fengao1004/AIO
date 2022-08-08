package com.goldze.mvvmhabit.aioui.knows

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.clazz.fragment.ClazzFragment
import com.goldze.mvvmhabit.aioui.knows.fragment.Knows2Fragment
import com.goldze.mvvmhabit.databinding.ActivityClazzBinding
import com.goldze.mvvmhabit.databinding.ActivityKnowsBinding
import me.goldze.mvvmhabit.base.BaseActivity

class Knows2Activity : BaseActivity<ActivityKnowsBinding, Knows2ViewModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_knows
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("心理知识")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fm_root, Knows2Fragment(), "knows")
        transaction.commitAllowingStateLoss()
    }
}