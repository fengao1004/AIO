package com.goldze.mvvmhabit.aioui.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.main.fragment.IntroduceFragment
import com.goldze.mvvmhabit.aioui.main.fragment.MainFragment
import com.goldze.mvvmhabit.aioui.main.fragment.SupportFragment
import com.goldze.mvvmhabit.databinding.ActivityMainBinding
import me.goldze.mvvmhabit.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    var mainFg = MainFragment()
    var supportFg = SupportFragment()
    var introduceFg = IntroduceFragment()

    companion object {
        const val FG_TAG_introduce = 0
        const val FG_TAG_MAIN = 1
        const val FG_TAG_SUPPORT = 2
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        showFg(FG_TAG_MAIN)
        viewModel.fgLiveData.observe(this) {
            showFg(it)
        }
    }

    fun showFg(fgTag: Int) {
        var tag = when (fgTag) {
            FG_TAG_introduce -> "introduce"
            FG_TAG_MAIN -> "main"
            FG_TAG_SUPPORT -> "support"
            else -> "main"
        }
        var fg = when (fgTag) {
            FG_TAG_introduce -> introduceFg
            FG_TAG_MAIN -> mainFg
            FG_TAG_SUPPORT -> supportFg
            else -> mainFg
        }
        val transaction = supportFragmentManager.beginTransaction()
        var currentFragment = supportFragmentManager.findFragmentByTag(tag)
        if (currentFragment != null) {
            hideAllFragment()
            transaction.show(currentFragment)
        } else {
            transaction.add(R.id.frameLayout, fg, tag)
        }
        transaction.commitAllowingStateLoss()
    }


    private fun hideAllFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(mainFg)
        transaction.hide(supportFg)
        transaction.hide(introduceFg)
        transaction.commitAllowingStateLoss()
    }
}