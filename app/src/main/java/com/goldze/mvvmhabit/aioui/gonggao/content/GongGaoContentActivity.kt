package com.goldze.mvvmhabit.aioui.gonggao.content

import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityGonggaoContentBinding
import com.goldze.mvvmhabit.databinding.ActivityScanBinding
import me.goldze.mvvmhabit.base.BaseActivity

class GongGaoContentActivity : BaseActivity<ActivityGonggaoContentBinding, GongGaoContentModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_gonggao_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        var id = intent.getStringExtra("id")
        binding.brRootView.setPageTitle("系统公告")
        viewModel.html.observe(this) {
            binding.web.setInitialScale(250)
            binding.web.loadDataWithBaseURL(
                null,
                it,
                "text/html",
                "utf-8",
                null
            )
        }
        viewModel.loadData(id)

    }
}