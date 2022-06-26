package com.goldze.mvvmhabit.aioui.gonggao

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.main.bean.GetAnnounListRequestBean
import com.goldze.mvvmhabit.aioui.main.bean.GetAnnounListRequestBeanRequestBody
import com.goldze.mvvmhabit.aioui.main.bean.GetAnnounListRequestBeanRequestHeader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class GonggaoViewModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    //下拉刷新
    var onRefreshCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        refresh()
    })

    private fun refresh() {
        Log.i("fengao_xiaomi", "refresh: ")
    }

    private fun loadMoreData() {
        Log.i("fengao_xiaomi", "loadMoreData: ")
    }

    //上拉加载
    var onLoadMoreCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        loadMoreData()
    })

    //给RecyclerView添加ObservableList
    var observableList: ObservableList<GonggaoRVModel> = ObservableArrayList()

    //给RecyclerView添加ItemBinding
    var itemBinding = ItemBinding.of<GonggaoRVModel>(BR.viewModel, R.layout.item_gonggao_rv)

    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun loadList(){
        var bean = GetAnnounListRequestBean(
            GetAnnounListRequestBeanRequestBody(0, 0, 100, "", 1),
            GetAnnounListRequestBeanRequestHeader(
                Util.serialNumber, Util.uniqueCode
            )
        )
        model.api.getAnnounList(bean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success && it.data.records.isNotEmpty()) {
                   it.data.records.forEach {
                       observableList.add(GonggaoRVModel(this,it))
                   }
                } else {
                    ToastUtils.showShort("获取公告失败")
                }
            }, {
                ToastUtils.showShort("获取公告失败")
                it.printStackTrace()
            })
    }
}