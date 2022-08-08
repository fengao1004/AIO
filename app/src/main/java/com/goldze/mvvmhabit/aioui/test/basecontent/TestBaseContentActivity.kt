package com.goldze.mvvmhabit.aioui.test.basecontent

import android.os.Bundle
import android.util.Log
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.test.bean.BasicDetailsResponseBeanData
import com.goldze.mvvmhabit.aioui.test.bean.FunnyTestBean
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import com.goldze.mvvmhabit.databinding.ActivityTestBaseContentBinding
import me.goldze.mvvmhabit.base.BaseActivity

class TestBaseContentActivity :
    BaseActivity<ActivityTestBaseContentBinding, TestBaseContentModel>() {

    lateinit var detail: ScaDetailsResponseBean
    lateinit var detail2: FunnyTestBean
    lateinit var basicBean: BasicDetailsResponseBeanData
    lateinit var marry: String
    lateinit var type: String
    lateinit var name: String
    lateinit var sex: String
    lateinit var scaRecId: String

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_test_base_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        basicBean = intent.getSerializableExtra("basebean") as BasicDetailsResponseBeanData
        detail = intent.getSerializableExtra("bean") as ScaDetailsResponseBean
        type = intent.getStringExtra("type")
        name = intent.getStringExtra("name")
        marry = intent.getStringExtra("marry")
        scaRecId = intent.getStringExtra("scaRecId")
        sex = intent.getStringExtra("sex")
        binding.brRootView.setPageTitle("基础题")
        viewModel.detail = detail
        viewModel.type = type
        viewModel.marry = marry
        viewModel.name = name
        viewModel.sex = sex
        viewModel.basicBean = basicBean
        Log.i("fengao_xiaomi", "marry: $marry ")
        Log.i("fengao_xiaomi", "sex: $sex")
        viewModel.init()
    }
}