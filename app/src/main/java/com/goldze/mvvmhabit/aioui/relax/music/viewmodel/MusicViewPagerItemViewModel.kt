package com.goldze.mvvmhabit.aioui.relax.music.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.TypeResponseBeanData
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.bean.list.MusicRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIORecyclerViewItemViewModel
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerFragmentModel
import com.goldze.mvvmhabit.aioui.http.impl.MusicRepository
import com.goldze.mvvmhabit.data.DemoRepository
import com.goldze.mvvmhabit.entity.DemoEntity
import io.reactivex.android.MainThreadDisposable
import io.reactivex.observers.DisposableObserver
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.goldze.mvvmhabit.http.BaseResponse
import me.goldze.mvvmhabit.http.ResponseThrowable
import me.goldze.mvvmhabit.utils.RxUtils
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MusicViewPagerItemViewModel(
    viewModel: MusicModel,
    application: Application,
    repository: MusicRepository
) :
    BaseViewModel<MusicRepository>(application, repository) {


    // 若有对应 tabBean 数据，要设置 tabBean
    var tabBean: TypeResponseBeanData? = null

    var parentViewModel: MusicModel? = viewModel

    //封装一个界面发生改变的观察者
    var uiChangeObservable = UIChangeObservable()

    class UIChangeObservable {
        //下拉刷新完成
        var finishRefreshing: SingleLiveEvent<*> = SingleLiveEvent<Any>()

        //上拉加载完成
        var finishLoadmore: SingleLiveEvent<*> = SingleLiveEvent<Any>()
    }

    //给RecyclerView添加ObservableList
    var observableList: ObservableList<MusicRvItemViewModel> = ObservableArrayList()

    //给RecyclerView添加ItemBinding
    var itemBinding = ItemBinding.of<MusicRvItemViewModel>(BR.viewModel, R.layout.item_rv_music)


    //下拉刷新
    var onRefreshCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        refreshData()
    })

    //上拉加载
    var onLoadMoreCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        loadMoreData()
    })

    var nextPage = 2

    @SuppressLint("CheckResult")
    private fun refreshData() {
        val requestBody = CommentRequestBean.getEmpty()
        requestBody.pageNum = 1
        var id = ""
        if (tabBean != null) {
            requestBody.typeId = tabBean?.id ?: ""
            requestBody.sysModuleTypeId = tabBean?.id ?: ""
            id = tabBean?.id ?: "null"
        }


        //模拟网络上拉加载更多
        model.getCommonListData(CommentRequestBean(requestBody, CommentRequestBean.getHeader()))
            .compose(RxUtils.schedulersTransformer()) //线程调度
            .doOnSubscribe(this) //请求与ViewModel周期同步
            .doOnSubscribe {}
            .map {
                it as CommonListResponseBean<BaseRecord>
            }
            .subscribe(
                object : DisposableObserver<CommonListResponseBean<BaseRecord>>() {
                    override fun onNext(responseBean: CommonListResponseBean<BaseRecord>) {
                        // 校验数据
                        if (responseBean.code != "200") {
                            ToastUtils.showShort("请求失败： ${responseBean.code}")
                            return
                        }
                        val records = responseBean.data.records ?: return
                        if (records.isNullOrEmpty()) {
                            ToastUtils.showShort("暂无数据")
                            return
                        }
                        observableList.clear()
                        for (record in records) {
                            val itemViewModel =
                                MusicRvItemViewModel(this@MusicViewPagerItemViewModel, record)
                            //双向绑定动态添加Item
                            observableList.add(itemViewModel)
                        }
                        Log.i("fengao_xiaomi", "onNext: ${observableList.size}")
                        nextPage = 2
                        //刷新完成收回
                        uiChangeObservable.finishLoadmore.call()
                        uiChangeObservable.finishRefreshing.call()
                        Log.i("fengao_xiaomi", "onNext: $id")
                        Log.i("fengao_xiaomi", "onNext: ${parentViewModel?.playId}")
                        var handle = Handler(Looper.myLooper()){
                            if (id == "null" && parentViewModel?.playId?.isNotEmpty() == true) {
                                records.forEachIndexed { index, baseRecord ->
                                    Log.i("fengao_xiaomi", "onNext: ${baseRecord.id}")
                                    if (baseRecord.id.toString() == parentViewModel!!.playId) {
                                        (records[index] as MusicRecord).itemPosition = index
                                        parentViewModel?.itemClickEvent?.value = records[index]
                                        parentViewModel?.playId = ""
                                        return@forEachIndexed
                                    }
                                }
                            }else if (id == "null" && parentViewModel?.playId?.isEmpty() == true) {
                                (records[0] as MusicRecord).itemPosition = 0
                                parentViewModel?.itemClickEvent?.value = records[0]
                            }

                            return@Handler true
                        }
                        handle.sendEmptyMessageDelayed(0,500)
                    }

                    override fun onError(throwable: Throwable) {
                        throwable.printStackTrace()
                        Log.i("fengao_xiaomi", "onError: ${throwable.message}")
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
            requestBody.typeId = tabBean?.id ?: ""
            requestBody.sysModuleTypeId = tabBean?.id ?: ""
        }

        //模拟网络上拉加载更多
        model.getCommonListData(CommentRequestBean(requestBody, CommentRequestBean.getHeader()))
            .compose(RxUtils.schedulersTransformer()) //线程调度
            .doOnSubscribe(this) //请求与ViewModel周期同步
            .doOnSubscribe {}
            .map {
                it as CommonListResponseBean<BaseRecord>
            }
            .subscribe(
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
                            val itemViewModel =
                                MusicRvItemViewModel(this@MusicViewPagerItemViewModel, record)
                            //双向绑定动态添加Item
                            observableList.add(itemViewModel)
                        }
                        Log.i("fengao_xiaomi", "onNext: ${observableList.size}")
                        nextPage++
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

    /**
     * 获取条目下标
     *
     * @param netWorkItemViewModel
     * @return
     */
    fun getItemPosition(netWorkItemViewModel: MusicRvItemViewModel?): Int {
        return observableList.indexOf(netWorkItemViewModel)
    }

    fun onItemClick(entity: BaseRecord) {
        parentViewModel?.itemClickEvent?.value = entity
    }
}