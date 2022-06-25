package com.goldze.mvvmhabit.aioui.test.content

import android.os.Bundle
import android.util.Log
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import com.goldze.mvvmhabit.databinding.ActivityTestContentBinding
import me.goldze.mvvmhabit.base.BaseActivity

class TestContentActivity : BaseActivity<ActivityTestContentBinding, TestContentModel>() {

    lateinit var detail: ScaDetailsResponseBean
    lateinit var marry: String
    lateinit var sex: String

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_test_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        detail = intent.getSerializableExtra("bean") as ScaDetailsResponseBean
        marry = intent.getStringExtra("marry")
        sex = intent.getStringExtra("sex")
        viewModel.detail = detail
        viewModel.marry = marry
        viewModel.sex = sex
        Log.i("fengao_xiaomi", "marry: $marry ")
        Log.i("fengao_xiaomi", "sex: $sex")
        binding.brRootView.setPageTitle(detail.data.scaVo.name)
        viewModel.init()
    }
}