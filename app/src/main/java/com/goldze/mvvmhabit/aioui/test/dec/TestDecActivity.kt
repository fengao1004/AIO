package com.goldze.mvvmhabit.aioui.test.dec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.scan.ScanModel
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import com.goldze.mvvmhabit.databinding.ActivityScanBinding
import com.goldze.mvvmhabit.databinding.ActivityTestDecBinding
import me.goldze.mvvmhabit.base.BaseActivity

class TestDecActivity : BaseActivity<ActivityTestDecBinding, TestDecModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_test_dec
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
//        viewModel.detail = detail

        var code = intent.getStringExtra("code")
        var name =  intent.getStringExtra("name")
        var type =  intent.getStringExtra("type")
        if(type == "normal"){
            binding.brRootView.setPageTitle("专业测评")
        }else{
            binding.brRootView.setPageTitle("趣味测评")
        }
        viewModel.loadData(code,name,type)
    }
}