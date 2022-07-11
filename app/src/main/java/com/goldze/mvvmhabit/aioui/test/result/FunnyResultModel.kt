package com.goldze.mvvmhabit.aioui.test.result

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.bean.TextObserver
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
class FunnyResultModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var title: TextObserver = TextObserver("")

    var result: TextObserver = TextObserver("")

    var again = BindingCommand<String>(BindingAction {
        activity.finish()
    })
}