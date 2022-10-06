package com.goldze.mvvmhabit.aioui.scan

import android.app.Application
import android.content.Intent
import android.util.Log
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.kepu.content.KepuContentActivity
import com.goldze.mvvmhabit.aioui.scan.qingxu.QingxuActivity
import com.goldze.mvvmhabit.aioui.scan.yinsi.YinsiActivity
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class ScanModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var agree: Boolean = false
    var start: BindingCommand<String> = BindingCommand(BindingAction {
        if (!agree) {
            ToastUtils.showShort("请先同意隐私协议")
        } else {
            startActivity(QingxuActivity::class.java)
        }
    })

    var check: BindingCommand<String> = BindingCommand(BindingAction {
        agree = !agree
        (activity as ScanActivity).setCheck(agree)
    })

    var yinsi: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(YinsiActivity::class.java)
    })
}