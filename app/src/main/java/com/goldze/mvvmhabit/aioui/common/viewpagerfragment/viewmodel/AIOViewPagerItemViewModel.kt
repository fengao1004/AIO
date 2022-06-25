package com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.entity.DemoEntity
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * 一个该 viewModel 对应一个 viewpager 页面(item)
 */
class AIOViewPagerItemViewModel(
    viewModel: AIOViewPagerFragmentModel,
    application: Application,
    repository: HttpRepository,
    itemViewResId: Int
) :
    BaseViewModel<HttpRepository>(application, repository) {

    var parentViewModel: AIOViewPagerFragmentModel? = viewModel

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
        loadMoreData()
    })

    //上拉加载
    var onLoadMoreCommand: BindingCommand<*> = BindingCommand<Any?>(BindingAction {
        loadMoreData()
    })

    private fun loadMoreData() {
        ToastUtils.showLong("加载ing")
        Thread {
            Thread.sleep(500)
            object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message?) {
                    super.handleMessage(msg)
                    for (i in 0 until 7) {
                        observableList.add(
                            AIORecyclerViewItemViewModel(
                                this@AIOViewPagerItemViewModel,
                                DemoEntity.ItemsEntity()
                            )
                        )
                        uiChangeObservable.finishLoadmore.call()
                        uiChangeObservable.finishRefreshing.call()
                    }
                }
            }.sendEmptyMessage(0)

        }.start()
    }

    fun onItemClick(entity: DemoEntity.ItemsEntity) {
        parentViewModel?.itemClickEvent?.value = entity
    }
}