package com.goldze.mvvmhabit.aioui.clazz.vm

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.TypeResponseBeanData
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ClazzViewPagerItemViewModel(application: Application, var bean: TypeResponseBeanData) :
    BaseViewModel<HttpRepository>(application) {
    var page = 0

    //封装一个界面发生改变的观察者
    var uiChangeObservable = UIChangeObservable()

    class UIChangeObservable {
        //下拉刷新完成
        var finishRefreshing: SingleLiveEvent<*> = SingleLiveEvent<Any>()

        //上拉加载完成
        var finishLoadmore: SingleLiveEvent<*> = SingleLiveEvent<Any>()
    }


    //给RecyclerView添加ObservableList
    var observableList: ObservableList<ClazzRvItemViewModel> = ObservableArrayList()

    //给RecyclerView添加ItemBinding
    var itemBinding = ItemBinding.of<ClazzRvItemViewModel>(BR.viewModel, R.layout.item_clazz_rv)

    //下拉刷新
    var onRefreshCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        page = 0
        requestNetWork()
    })

    //上拉加载
    var onLoadMoreCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        requestNetWork()
    })

    init {
        model = HttpRepository()
        requestNetWork()
    }


    /**
     * 网络请求方法，在ViewModel中调用Model层，通过Okhttp+Retrofit+RxJava发起请求
     */
    @SuppressLint("CheckResult")
    fun requestNetWork() {
        var empty = CommentRequestBean.getEmpty()
        empty.id = this.bean.id.toLong()
        empty.pageNum = page++
        var bean = CommentRequestBean(
            empty, CommentRequestBean.getHeader()
        )
        model.api.getClazzList(bean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                uiChangeObservable.finishRefreshing.call()
                uiChangeObservable.finishLoadmore.call()
                if (it.success) {
                    try {
                        if (page == 1) {
                            observableList.clear()
                        }
                        var temp: ClazzRvItemViewModel? = if (observableList.isEmpty()) {
                            null
                        } else {
                            var endInfo = observableList.get(observableList.size - 1)
                            if (endInfo.right != null) {
                                null
                            } else {
                                endInfo
                            }
                        }
                        it.data.records.forEach {
                            if (temp == null) {
                                temp = ClazzRvItemViewModel(this)
                                temp?.left = it
                                observableList.add(temp)
                            } else {
                                temp?.right = it
                                temp = null
                            }
                        }
                    }catch (e:Exception){
                        Log.i("fengao_xiaomi", "requestNetWork: $e")
                    }
                }
            }, {
                Log.i("fengao_xiaomi", "requestNetWork: $it")
                uiChangeObservable.finishRefreshing.call()
                uiChangeObservable.finishLoadmore.call()
            })
    }

    var text: String? = null

    init {
        this.text = bean.name
    }
}