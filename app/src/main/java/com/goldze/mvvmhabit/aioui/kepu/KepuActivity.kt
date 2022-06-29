package com.goldze.mvvmhabit.aioui.kepu

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityKepuBinding
import me.goldze.mvvmhabit.base.BaseActivity

class KepuActivity : BaseActivity<ActivityKepuBinding,KepuViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_kepu
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("常见问题科普专题")
        viewModel.loadEndLD.observe(this) {
            binding.twinklingRefreshLayout.finishLoadmore()
            binding.twinklingRefreshLayout.finishRefreshing()
        }
        binding.twinklingRefreshLayout.startRefresh()
    }
}