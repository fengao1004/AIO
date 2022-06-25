package com.goldze.mvvmhabit.aioui.test.dec

import android.app.Application
import android.content.Intent
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import com.goldze.mvvmhabit.aioui.test.content.TestContentActivity
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class TestDecModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var url =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1113%2F0F420110430%2F200F4110430-6-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1658728824&t=3c80f1fe4665ce36ff2063dc4b985308"
    var startTest = BindingCommand<String>(BindingAction {
        var intent = Intent(application, TestContentActivity::class.java)
        intent.putExtra("bean", detail)
        application.startActivity(intent)
    })
    var detail: ScaDetailsResponseBean? = null
}