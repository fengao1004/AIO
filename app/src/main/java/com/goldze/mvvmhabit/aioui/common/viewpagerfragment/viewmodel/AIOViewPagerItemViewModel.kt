package com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.TypeResponseBeanData
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.http.ListRepository
import io.reactivex.observers.DisposableObserver
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.goldze.mvvmhabit.http.ResponseThrowable
import me.goldze.mvvmhabit.utils.RxUtils
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * 一个该 viewModel 对应一个 viewpager 页面(item)
 */
class AIOViewPagerItemViewModel(
    val parentViewModel: AIOViewPagerFragmentModel,
    application: Application,
    repository: ListRepository,
    itemViewResId: Int
) :
    BaseViewModel<ListRepository>(application, repository) {

    // 若有对应 tabBean 数据，要设置 tabBean
    var tabBean: TypeResponseBeanData? = null

    //封装一个界面发生改变的观察者
    var uiChangeObservable = UIChangeObservable()

    class UIChangeObservable {
        //下拉刷新完成
        var finishRefreshing: SingleLiveEvent<*> = SingleLiveEvent<Any>()

        //上拉加载完成
        var finishLoadmore: SingleLiveEvent<*> = SingleLiveEvent<Any>()
    }

    //给RecyclerView添加ObservableList
    var observableList: ObservableList<AIORecyclerViewItemViewModel> = ObservableArrayList()

    //给RecyclerView添加ItemBinding
    var itemBinding = ItemBinding.of<AIORecyclerViewItemViewModel>(BR.viewModel, itemViewResId)


    //下拉刷新
    var onRefreshCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        refreshData()
    })

    //上拉加载
    var onLoadMoreCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        loadMoreData()
    })

    var nextPage = 1

    @SuppressLint("CheckResult")
    private fun refreshData() {
        val requestBody = CommentRequestBean.getEmpty()
        requestBody.pageNum = 1
        if (tabBean != null) {
            requestBody.id = tabBean?.id?.toLong() ?: 0
        }

        model.getCommonListData(CommentRequestBean(requestBody, CommentRequestBean.getHeader()))
            .compose(RxUtils.schedulersTransformer()) //线程调度
            .doOnSubscribe(this) //请求与ViewModel周期同步
            .doOnSubscribe {}
            .map {
                it as CommonListResponseBean<BaseRecord>
            }.subscribe(
                object : DisposableObserver<CommonListResponseBean<BaseRecord>>() {
                    override fun onNext(responseBean: CommonListResponseBean<BaseRecord>) {
                        // 校验数据
                        if (responseBean.code != "200") {
                            ToastUtils.showShort("请求失败： ${responseBean.code}")
                            return
                        }
                        val records = responseBean.data.records ?: return
                        if (records.isNullOrEmpty()) {
                            ToastUtils.showShort("没有更多数据")
                            return
                        }

                        for (record in records) {
                            val itemViewModel = AIORecyclerViewItemViewModel(this@AIOViewPagerItemViewModel, record)
                            //双向绑定动态添加Item
                            observableList.clear()
                            nextPage = 2
                            observableList.add(itemViewModel)
                        }
                        //刷新完成收回
                        uiChangeObservable.finishLoadmore.call()
                        uiChangeObservable.finishRefreshing.call()
                    }

                    override fun onError(throwable: Throwable) {
                        if (throwable is ResponseThrowable) {
                            ToastUtils.showShort((throwable as ResponseThrowable).message)
                        }
                        //刷新完成收回
                        uiChangeObservable.finishLoadmore.call()
                        uiChangeObservable.finishRefreshing.call()
                    }

                    override fun onComplete() {
                        //刷新完成收回
                        uiChangeObservable.finishLoadmore.call()
                        uiChangeObservable.finishRefreshing.call()
                    }
                })
    }

    @SuppressLint("CheckResult")
    private fun loadMoreData() {
        val requestBody = CommentRequestBean.getEmpty()
        requestBody.pageNum = nextPage
        if (tabBean != null) {
            requestBody.id = tabBean?.id?.toLong() ?: 0
        }

        model.getCommonListData(CommentRequestBean(requestBody, CommentRequestBean.getHeader()))
            .compose(RxUtils.schedulersTransformer()) //线程调度
            .doOnSubscribe(this) //请求与ViewModel周期同步
            .doOnSubscribe {}
            .map {
                it as CommonListResponseBean<BaseRecord>
            }.subscribe(
                object : DisposableObserver<CommonListResponseBean<BaseRecord>>() {
                    override fun onNext(responseBean: CommonListResponseBean<BaseRecord>) {
                        // 校验数据
                        if (responseBean.code != "200") {
                            ToastUtils.showShort("请求失败： ${responseBean.code}")
                            return
                        }
                        val records = responseBean.data.records ?: return
                        if (records.isNullOrEmpty()) {
                            ToastUtils.showShort("没有更多数据")
                            return
                        }
                        for (record in records) {
                            val itemViewModel = AIORecyclerViewItemViewModel(this@AIOViewPagerItemViewModel, record)
                            //双向绑定动态添加Item
                            nextPage++
                            observableList.add(itemViewModel)
                        }
                        //刷新完成收回
                        uiChangeObservable.finishLoadmore.call()
                        uiChangeObservable.finishRefreshing.call()
                    }

                    override fun onError(throwable: Throwable) {
                        if (throwable is ResponseThrowable) {
                            ToastUtils.showShort((throwable as ResponseThrowable).message)
                        }
                        //刷新完成收回
                        uiChangeObservable.finishLoadmore.call()
                        uiChangeObservable.finishRefreshing.call()
                    }

                    override fun onComplete() {
                        //刷新完成收回
                        uiChangeObservable.finishLoadmore.call()
                        uiChangeObservable.finishRefreshing.call()
                    }
                })
    }

    fun onItemClick(entity: BaseRecord) {
        parentViewModel.itemClickEvent.value = entity
    }
}