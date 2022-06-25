package com.goldze.mvvmhabit.aioui.test

import android.content.Intent
import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.main.MainActivity
import com.goldze.mvvmhabit.aioui.main.fragment.IntroduceFragment
import com.goldze.mvvmhabit.aioui.main.fragment.SupportFragment
import com.goldze.mvvmhabit.aioui.test.dec.TestDecActivity
import com.goldze.mvvmhabit.databinding.ActivityKnowsBinding
import com.goldze.mvvmhabit.databinding.ActivityTestBinding
import me.goldze.mvvmhabit.base.BaseActivity

class TestActivity : BaseActivity<ActivityTestBinding, TestModel>() {
    var testA = TestAFragment()
    var testB = TestBFragment()

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_test
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("专业评测")
        binding.brRootView.hidePageTitle()
        viewModel.isSpecialty.observe(this, {
            showFg(it)
        })
        viewModel.loadData()
    }

    fun showFg(fgTag: Boolean) {
        var tag = if (fgTag) {
            "Specialty"
        } else {
            "Normal"
        }
        var fg = if (fgTag) {
            testA
        } else {
            testB
        }
        val transaction = supportFragmentManager.beginTransaction()
        var currentFragment = supportFragmentManager.findFragmentByTag(tag)
        if (currentFragment != null) {
            hideAllFragment()
            transaction.show(currentFragment)
        } else {
            transaction.add(R.id.fl_content, fg, tag)
        }
        transaction.commitAllowingStateLoss()
        viewModel.detailLiveData.observe(this) {
            var intent = Intent(this, TestDecActivity::class.java)
            intent.putExtra("bean", it)
            startActivity(intent)
        }
    }

    private fun hideAllFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(testA)
        transaction.hide(testB)
        transaction.commitAllowingStateLoss()
    }

}