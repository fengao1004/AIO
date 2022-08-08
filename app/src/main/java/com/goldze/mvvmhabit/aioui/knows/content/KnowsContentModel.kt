package com.goldze.mvvmhabit.aioui.knows.content

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.knows.KnowsBeanRecord
import com.goldze.mvvmhabit.aioui.knows.KnowsRecord
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class KnowsContentModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var bean: KnowsRecord? = null
    var countNum = "点击数量："
    var createTime = "创建时间："
    fun setKnowBean(bean : KnowsRecord){
        this.bean = bean
        countNum += bean.clickCount
        createTime += bean.createTime
    }
}