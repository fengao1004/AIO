package com.goldze.mvvmhabit.aioui.relax.music.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIORecyclerViewItemViewModel
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerFragmentModel
import com.goldze.mvvmhabit.aioui.http.impl.MusicRepository
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

class MusicViewPagerItemViewModel(viewModel: MusicModel, application: Application, repository: MusicRepository) :
    BaseViewModel<MusicRepository>(application, repository) {

    var parentViewModel: MusicModel? = viewModel

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

    @SuppressLint("CheckResult")
    private fun loadMoreData() {
        if (observableList.size > 50) {
            ToastUtils.showLong("兄dei，你太无聊啦~崩是不可能的~")
            uiChangeObservable.finishLoadmore.call()
            return
        }
        //模拟网络上拉加载更多
        model.getCommonListData((CommentRequestBean(CommentRequestBean.getEmpty(), CommentRequestBean.getHeader())))
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
                            val itemViewModel = MusicRvItemViewModel(this@MusicViewPagerItemViewModel, record)
                            //双向绑定动态添加Item
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

    fun onItemClick(entity: BaseRecord) {
        parentViewModel?.itemClickEvent?.value = entity
    }
}