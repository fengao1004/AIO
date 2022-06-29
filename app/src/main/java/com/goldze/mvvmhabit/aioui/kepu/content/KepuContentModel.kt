package com.goldze.mvvmhabit.aioui.kepu.content

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.kepu.KepuBeanRecord
import com.goldze.mvvmhabit.aioui.knows.KnowsBeanRecord
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
class KepuContentModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var bean: KepuBeanRecord? = null
    var dataLiveData = MutableLiveData<KepuItemBeanData>()
    var countNum = "点击数量："
    var createTime = "创建时间："
    fun setKnowBean(bean: KepuBeanRecord) {
        this.bean = bean
//        countNum += bean.clickCount
//        createTime += bean.createTime
    }

    init {
        model = HttpRepository()
    }


    fun loadData() {
        if (bean == null) {
            return
        }
        var body = CommentRequestBean.getEmpty()
        body.themeId = bean?.id?.toInt()!!
        var requestBean = CommentRequestBean(body, CommentRequestBean.getHeader())
        model.api.getThemeItemList(requestBean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    dataLiveData.postValue(it.data)
                } else {
                    ToastUtils.showShort("数据加载失败")
                }
            }, {
                ToastUtils.showShort("网络错误！")
            })

    }
}