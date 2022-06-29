package com.goldze.mvvmhabit.aioui.zixun

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.zixun.phone.PhoneListActivity
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class ZixunModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var clickPhone = BindingCommand<String>(BindingAction {
        activity.startActivity(Intent(activity, PhoneListActivity::class.java))
    })
    var clickInput = BindingCommand<String>(BindingAction {

    })
}