package com.goldze.mvvmhabit.aioui.scan.qrcode

import android.os.Bundle
import android.widget.ImageView
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.scan.qingxu.QingxuActivity
import com.goldze.mvvmhabit.databinding.ActivityQrCodeBinding
import me.goldze.mvvmhabit.base.BaseActivity


class QRCodeActivity : BaseActivity<ActivityQrCodeBinding, QRCodeModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_qr_code
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        // 取链接、拼接 source 打开来源
        val url = intent.getStringExtra("url")
        viewModel.mobileUrl = "$url&source=mobile"
        viewModel.deviceUrl = "$url&source=device"
        viewModel.reportId = intent.getLongExtra("id", 0L)

        initView()
    }

    private fun initView() {
        binding.apply {
            brRootView.setPageTitle("报告")
            val ivBack = brRootView.findViewById<ImageView>(R.id.iv_back)
            ivBack.setOnClickListener {
                gotoScan()
            }
            Thread {
                val urlQRCode = viewModel?.getUrlQRCode()
                runOnUiThread {
                    qrCode.setImageBitmap(urlQRCode)
                }
            }.start()
        }
    }

    fun gotoScan() {
        startActivity(QingxuActivity::class.java)
        finish()
    }


}