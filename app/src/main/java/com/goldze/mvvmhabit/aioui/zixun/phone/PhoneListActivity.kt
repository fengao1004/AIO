package com.goldze.mvvmhabit.aioui.zixun.phone

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityPhoneListBinding
import com.goldze.mvvmhabit.databinding.ActivityScanBinding
import me.goldze.mvvmhabit.base.BaseActivity

class PhoneListActivity : BaseActivity<ActivityPhoneListBinding, PhoneListModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_phone_list
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("电话咨询")
        viewModel.loadData()
    }
}