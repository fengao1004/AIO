package com.goldze.mvvmhabit.aioui.notice

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityKnowsBinding
import com.goldze.mvvmhabit.databinding.ActivityNoticeBinding
import me.goldze.mvvmhabit.base.BaseActivity

class NoticeActivity : BaseActivity<ActivityNoticeBinding, NoticeModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_notice
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("公告")
    }

}