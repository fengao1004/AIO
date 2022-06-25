package com.goldze.mvvmhabit.aioui.relax.music.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.data.DemoRepository
import com.goldze.mvvmhabit.entity.DemoEntity
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

class MusicViewPagerItemViewModel(viewModel: MusicModel, application: Application, repository: DemoRepository) :
    BaseViewModel<DemoRepository>(application, repository) {



    var deleteItemLiveData = SingleLiveEvent<MusicRvItemViewModel>()

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
        loadMoreData()
    })

    //上拉加载
    var onLoadMoreCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        loadMoreData()
    })

    private fun loadMoreData() {
        if (observableList.size > 50) {
            ToastUtils.showLong("兄dei，你太无聊啦~崩是不可能的~")
            uiChangeObservable.finishLoadmore.call()
            return
        }
        //模拟网络上拉加载更多
        model.loadMore()
            .compose(RxUtils.schedulersTransformer()) //线程调度
            .doOnSubscribe(this) //请求与ViewModel周期同步
            .doOnSubscribe {}
            .map {
                it as DemoEntity
            }
            .subscribe { entity ->
                for (itemsEntity in entity.items) {
                    val itemViewModel = MusicRvItemViewModel(this@MusicViewPagerItemViewModel, itemsEntity)
                    //双向绑定动态添加Item
                    observableList.add(itemViewModel)
                }
                //刷新完成收回
                uiChangeObservable.finishLoadmore.call()
            }
    }


    /**
     * 网络请求方法，在ViewModel中调用Model层，通过Okhttp+Retrofit+RxJava发起请求
     */
    fun requestNetWork() {
        //可以调用addSubscribe()添加Disposable，请求与View周期同步
        model.demoGet()
            .compose(RxUtils.schedulersTransformer()) //线程调度
            .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
            .doOnSubscribe(this) //请求与ViewModel周期同步
            .doOnSubscribe { showDialog("正在请求...") }
            .map {
                it as BaseResponse<DemoEntity>
            }
            .subscribe(object : DisposableObserver<BaseResponse<DemoEntity>>() {
                override fun onNext(response: BaseResponse<DemoEntity>) {
                    //清除列表
                    observableList.clear()
                    //请求成功
                    if (response.code == 1) {
                        for (entity in response.result!!.items) {
                            val itemViewModel = MusicRvItemViewModel(this@MusicViewPagerItemViewModel, entity)
                            //双向绑定动态添加Item
                            observableList.add(itemViewModel)
                        }
                    } else {
                        //code错误时也可以定义Observable回调到View层去处理
                        ToastUtils.showShort("数据错误")
                    }
                }

                override fun onError(throwable: Throwable) {
                    //请求刷新完成收回
                    uiChangeObservable.finishRefreshing.call()
                    if (throwable is ResponseThrowable) {
                        ToastUtils.showShort((throwable as ResponseThrowable).message)
                    }
                }

                override fun onComplete() {
                    //请求刷新完成收回
                    uiChangeObservable.finishRefreshing.call()
                }
            })
    }


    /**
     * 删除条目
     *
     * @param netWorkItemViewModel
     */
    fun deleteItem(netWorkItemViewModel: MusicRvItemViewModel?) {
        //点击确定，在 observableList 绑定中删除，界面立即刷新
        observableList.remove(netWorkItemViewModel)
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


    var text: String? = null
    var onItemClick: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        //点击之后通过 livedata 将逻辑转到 activity 中的观察者处理
        viewModel.itemClickEvent.value = text
    })
}