package com.goldze.mvvmhabit.aioui.clazz.content

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.clazz.bean.ClazzResponseBeanData
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
class ClazzContentModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var bean: ClazzResponseBeanData? = null
    var selectType = ObservableInt(0)
    var beanLiveData: MutableLiveData<ClazzResponseBeanData> = MutableLiveData()

    var clickJieshao = BindingCommand<String>(BindingAction {
        selectType.set(1)
    })
    var clickMulu = BindingCommand<String>(BindingAction {
        selectType.set(0)
    })

    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun loadData(id: String) {
        var empty = CommentRequestBean.getEmpty()
        empty.id = id
        var header = CommentRequestBean.getHeader()
        model.api.getClazzDetail(CommentRequestBean(empty, header))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    bean = it.data
                    beanLiveData.postValue(it.data)
                } else {
                    ToastUtils.showShort("加载课程详情失败 ${it.message}")
                }
            }, {
                ToastUtils.showShort("加载课程详情失败 ${it.message}")
            })
    }
}