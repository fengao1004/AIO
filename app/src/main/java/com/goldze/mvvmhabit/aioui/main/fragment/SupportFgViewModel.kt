package com.goldze.mvvmhabit.aioui.main.fragment

import android.app.Application
import android.util.Log
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
class SupportFgViewModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    
    var testClick: BindingCommand<String> = BindingCommand(BindingAction {
        Log.i("fengao_xiaomi", ": ")
    })
}