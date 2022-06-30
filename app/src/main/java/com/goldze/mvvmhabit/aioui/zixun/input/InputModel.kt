package com.goldze.mvvmhabit.aioui.zixun.input

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.bean.TextObserver
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
class InputModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun commit(zxfs: String, wtlx: String, yyrq: String, yysj: String, jtwt: String, yydd: String) {
        var bean = InputRequestBean(
            RequestBody(
                yydd, yyrq, yysj, zxfs, jtwt, 0, username, password, 0, 100, "", wtlx, 1, ""
            ), RequestHeader(Util.serialNumber, Util.uniqueCode)
        )
        model.api.commitInput(bean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    ToastUtils.showShort("预约成功")
                    activity.finish()
                } else {
                    ToastUtils.showShort("预约失败，请稍微再试 ${it.message}")
                }
            }, {
                ToastUtils.showShort("预约失败，请稍微再试 ${it.message}")
            })
    }

    lateinit var password: String
    lateinit var username: String
    var showLogin = ObservableBoolean(true)

    var zxfs: TextObserver = TextObserver("请选择")
    var wtlx: TextObserver = TextObserver("请选择")
    var jtwt: TextObserver = TextObserver("请选择")
    var yyrq: TextObserver = TextObserver("请选择")
    var yysj: TextObserver = TextObserver("请选择")
    var yydd: TextObserver = TextObserver("请选择")

}