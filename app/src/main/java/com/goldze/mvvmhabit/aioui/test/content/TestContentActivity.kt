package com.goldze.mvvmhabit.aioui.test.content

import android.os.Bundle
import android.util.Log
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.test.bean.FunnyTestBean
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import com.goldze.mvvmhabit.databinding.ActivityTestContentBinding
import me.goldze.mvvmhabit.base.BaseActivity

class TestContentActivity : BaseActivity<ActivityTestContentBinding, TestContentModel>() {

    lateinit var detail: ScaDetailsResponseBean
    lateinit var detail2: FunnyTestBean
    lateinit var marry: String
    lateinit var type: String
    lateinit var name: String
    lateinit var sex: String

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_test_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        type = intent.getStringExtra("type")
        name = intent.getStringExtra("name")

        if (type == "normal") {
            detail = intent.getSerializableExtra("bean") as ScaDetailsResponseBean
            binding.brRootView.setPageTitle(detail.data.scaVo.name)
        } else {
            detail2 = intent.getSerializableExtra("bean") as FunnyTestBean
            binding.brRootView.setPageTitle(detail2.data.name)
        }
        marry = intent.getStringExtra("marry")
        sex = intent.getStringExtra("sex")
        if (type == "normal") {
            viewModel.detail = detail
        } else {
            viewModel.detail2 = detail2
        }
        viewModel.type = type
        viewModel.marry = marry
        viewModel.name = name
        viewModel.sex = sex
        Log.i("fengao_xiaomi", "marry: $marry ")
        Log.i("fengao_xiaomi", "sex: $sex")
        viewModel.init()
    }
}