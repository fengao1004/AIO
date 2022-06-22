package com.goldze.mvvmhabit.aioui.test.dec

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.http.HttpRepository
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
    var startTest = BindingCommand<String>(BindingAction {
        startActivity(TestContentActivity::class.java)
    })
}