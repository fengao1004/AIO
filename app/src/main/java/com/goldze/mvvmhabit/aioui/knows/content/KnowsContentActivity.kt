package com.goldze.mvvmhabit.aioui.knows.content

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.knows.KnowsBeanRecord
import com.goldze.mvvmhabit.databinding.ActivityKnowsContentBinding
import com.goldze.mvvmhabit.databinding.ActivityScanBinding
import me.goldze.mvvmhabit.base.BaseActivity

class KnowsContentActivity : BaseActivity<ActivityKnowsContentBinding, KnowsContentModel>() {

    var bean: KnowsBeanRecord? = null
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_knows_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("情绪扫描")
        bean = intent.getSerializableExtra("bean") as KnowsBeanRecord
        viewModel.setKnowBean(bean!!)
    }
}