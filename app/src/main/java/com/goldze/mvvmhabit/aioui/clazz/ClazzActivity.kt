package com.goldze.mvvmhabit.aioui.clazz

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.clazz.fragment.ClazzFragment
import com.goldze.mvvmhabit.databinding.ActivityClazzBinding
import me.goldze.mvvmhabit.base.BaseActivity

class ClazzActivity : BaseActivity<ActivityClazzBinding, ClazzViewModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_clazz
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("心理课堂")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fm_root, ClazzFragment(), "clazz")
        transaction.commitAllowingStateLoss()
    }
}