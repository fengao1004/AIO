package com.goldze.mvvmhabit.aioui.knows.content

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.knows.KnowsBeanRecord
import com.goldze.mvvmhabit.aioui.knows.KnowsRecord
import com.goldze.mvvmhabit.databinding.ActivityKnowsContentBinding
import com.goldze.mvvmhabit.databinding.ActivityScanBinding
import me.goldze.mvvmhabit.base.BaseActivity

class KnowsContentActivity : BaseActivity<ActivityKnowsContentBinding, KnowsContentModel>() {

    var bean: KnowsRecord? = null
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_knows_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        bean = intent.getSerializableExtra("bean") as KnowsRecord
        binding.brRootView.setPageTitle("心理知识")
        viewModel.setKnowBean(bean!!)
        binding.webContent.setInitialScale(250)
        binding.webContent.loadDataWithBaseURL(
            null,
            bean?.infoDescribe ?: "",
            "text/html",
            "utf-8",
            null
        )
    }
}