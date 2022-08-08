package com.goldze.mvvmhabit.aioui.knows

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.google.gson.Gson
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
class KnowsModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var loadEndLD:MutableLiveData<String> = MutableLiveData()
    var page = 1

    //给RecyclerView添加ObservableList
    var observableList: ObservableList<KnowsRvItemViewModel> = ObservableArrayList()

    //给RecyclerView添加ItemBinding
    var itemBinding = ItemBinding.of<KnowsRvItemViewModel>(BR.viewModel, R.layout.item_konws_rv)

    //下拉刷新
    var onRefreshCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        page = 1
        requestNetWork()
    })

    //上拉加载
    var onLoadMoreCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        requestNetWork()
    })

    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun requestNetWork() {
//        var empty = CommentRequestBean.getEmpty()
//        empty.pageNum = page++
//        Log.i("fengao_xiaomi", "requestNetWork:${empty.pageNum} ")
//        var bean = CommentRequestBean(
//            empty, CommentRequestBean.getHeader()
//        )
//        model.api.getKnowsList(bean)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                loadEndLD.postValue("")
//                Log.i("fengao_xiaomi", "requestNetWork: ${Gson().toJson(it)}")
//                if (it.success) {
//                    if (page == 2) {
//                        observableList.clear()
//                    }
//                    var vm: KnowsRvItemViewModel? = if (observableList.isEmpty()) {
//                        null
//                    } else {
//                        if (observableList[observableList.size - 1].right == null) {
//                            observableList[observableList.size - 1]
//                        } else {
//                            null
//                        }
//                    }
//                    it.data.records.forEach {
//                        if (vm == null) {
//                            vm = KnowsRvItemViewModel(this)
//                            observableList.add(vm)
//                            vm?.left = it
//                        } else {
//                            vm?.right = it
//                            vm = null
//                        }
//                    }
//                } else {
//                    ToastUtils.showShort("加载错误")
//                }
//
//            }, {
//                loadEndLD.postValue("")
//                ToastUtils.showShort("加载错误")
//            })
    }
}