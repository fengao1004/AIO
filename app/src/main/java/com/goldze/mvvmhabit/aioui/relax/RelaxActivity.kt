package com.goldze.mvvmhabit.aioui.relax

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
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
        binding.brRootView.backIv.setOnClickListener {
            finish()
        }
        binding.brRootView.setPageTitle("减压调适")

        val transaction = supportFragmentManager.beginTransaction()
        var currentFragment = supportFragmentManager.findFragmentByTag("FragmentRelax")
        if (currentFragment != null) {
            transaction.show(currentFragment)
        } else {
            transaction.add(R.id.frameLayout, RelaxFragment(), "FragmentRelax")
        }
        transaction.commitAllowingStateLoss()
    }

}