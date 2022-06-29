package com.goldze.mvvmhabit.aioui.test.dec

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsRequestBean
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import com.goldze.mvvmhabit.aioui.test.content.TestContentActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    var detail: ScaDetailsResponseBean? = null

    //    var detail: ScaDetailsResponseBean? = null
    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun loadData(code: String?, name: String?) {
        model.api.getScaDetails(ScaDetailsRequestBean(scaCode = code!!))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    detail = it
                }
            }, {
                it.printStackTrace()
                Log.i("fengao_xiaomi", "loadData: ${it.message}")
            })
    }

    var showSexChoose = ObservableBoolean(false)
    var sex = ObservableInt(0) // 0 男 1 女
    var showMarryChoose = ObservableBoolean(false)
    var marry = ObservableInt(0) // 0 未婚 1 已婚 2 离婚 3 丧偶
    var url =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1113%2F0F420110430%2F200F4110430-6-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1658728824&t=3c80f1fe4665ce36ff2063dc4b985308"
    var startTest = BindingCommand<String>(BindingAction {
        showSexChoose.set(true)
    })

    var marryClickA = BindingCommand<String>(BindingAction {
        marry.set(0)
    })
    var marryClickB = BindingCommand<String>(BindingAction {
        marry.set(1)
    })
    var marryClickC = BindingCommand<String>(BindingAction {
        marry.set(2)
    })
    var marryClickD = BindingCommand<String>(BindingAction {
        marry.set(3)
    })
    var sexClickA = BindingCommand<String>(BindingAction {
        sex.set(0)
    })

    var sexClickB = BindingCommand<String>(BindingAction {
        sex.set(1)
    })
    var sexClick = BindingCommand<String>(BindingAction {
        showSexChoose.set(false)
        showMarryChoose.set(true)
    })
    var marryClick = BindingCommand<String>(BindingAction {
        var intent = Intent(application, TestContentActivity::class.java)
        intent.putExtra(
            "marry", when (marry.get()) {
                0 -> "未婚"
                1 -> "已婚"
                2 -> "离婚"
                3 -> "丧偶"
                else -> "未婚"
            }
        )
        intent.putExtra("bean", detail)
        intent.putExtra(
            "sex", when (sex.get()) {
                0 -> "男"
                1 -> "女"
                else -> "男"
            }
        )
        activity.finish()
        activity.startActivity(intent)
    })
}