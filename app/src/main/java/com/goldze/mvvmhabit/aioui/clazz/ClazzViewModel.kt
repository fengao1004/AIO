package com.goldze.mvvmhabit.aioui.clazz

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class ClazzViewModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var fgLiveData: MutableLiveData<Int> = MutableLiveData()

    var showSupport: BindingCommand<String> = BindingCommand(BindingAction {
        fgLiveData.postValue(2)
    })

    var showIntroduce: BindingCommand<String> = BindingCommand(BindingAction {
        fgLiveData.postValue(0)
    })
    var showMain: BindingCommand<String> = BindingCommand(BindingAction {
        fgLiveData.postValue(1)
    })

}