package com.goldze.mvvmhabit.aioui.test.result

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityResultBinding
import com.goldze.mvvmhabit.databinding.ActivityScanBinding
import me.goldze.mvvmhabit.base.BaseActivity

class FunnyResultActivity : BaseActivity<ActivityResultBinding, FunnyResultModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_result
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        var name = intent.getStringExtra("name")
        viewModel.title.value = "我的结果：" + intent.getStringExtra("title")

        viewModel.result.value = intent.getStringExtra("result")
        binding.brRootView.setPageTitle(name)
    }
}