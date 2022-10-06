package com.goldze.mvvmhabit.aioui.scan.qingxu

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.scan.qingxu.mvp.PresenterImpl
import com.goldze.mvvmhabit.aioui.scan.qingxu.mvp.QingxuFragment
import com.goldze.mvvmhabit.databinding.ActivityQingxuBinding
import me.goldze.mvvmhabit.base.BaseActivity

class QingxuActivity : BaseActivity<ActivityQingxuBinding, QingxuModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_qingxu
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("情绪分析")
        val transaction = supportFragmentManager.beginTransaction()
        var fragment = QingxuFragment()
        PresenterImpl(this, fragment)
        transaction.add(R.id.root, fragment, "knows")
        transaction.commitAllowingStateLoss()

    }
}