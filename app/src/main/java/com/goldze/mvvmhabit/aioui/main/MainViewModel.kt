package com.goldze.mvvmhabit.aioui.main

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.http.HttpRepository
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
class MainViewModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var fgLiveData: MutableLiveData<Int> = MutableLiveData()
    var showState = ObservableInt(1)
    var showSupport: BindingCommand<String> = BindingCommand(BindingAction {
        if (Util.shebeiXq == null) {
            ToastUtils.showShort("设备初始化中，请稍后再试")
        } else {
            fgLiveData.postValue(2)
            showState.set(2)
        }
    })

    var showIntroduce: BindingCommand<String> = BindingCommand(BindingAction {
        if (Util.shebeiXq == null) {
            ToastUtils.showShort("设备初始化中，请稍后再试")
        } else {
            fgLiveData.postValue(0)
            showState.set(0)
        }
    })

    var showMain: BindingCommand<String> = BindingCommand(BindingAction {
        fgLiveData.postValue(1)
        showState.set(1)
    })

}