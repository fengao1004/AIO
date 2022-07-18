package com.goldze.mvvmhabit.aioui.gonggao.content

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
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
class GongGaoContentModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var name = TextObserver("")
    var time = TextObserver("")
    var html = MutableLiveData<String>()

    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun loadData(id: String) {
        val empty = CommentRequestBean.getEmpty()
        empty.id = id
        var commonBean =
            CommentRequestBean(empty, CommentRequestBean.getHeader())
        model.api.getGonggaoContent(commonBean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    name.value = it.data.name
                    time.value = it.data.createTime
                    html.postValue(it.data.content)
                } else {
                    ToastUtils.showShort("公告加载失败" + it.message)
                }
            }, {
                ToastUtils.showShort("公告加载失败" + it.message)
            })
    }
}