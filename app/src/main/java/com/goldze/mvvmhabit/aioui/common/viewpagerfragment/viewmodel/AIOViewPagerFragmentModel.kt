package com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.TypeResponseBean
import com.goldze.mvvmhabit.aioui.bean.TypeResponseBeanData
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import io.reactivex.observers.DisposableObserver
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.goldze.mvvmhabit.http.ResponseThrowable
import me.goldze.mvvmhabit.utils.RxUtils
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter.PageTitles
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.util.ArrayList


class AIOViewPagerFragmentModel(application: Application) : BaseViewModel<HttpRepository>(application) {

    init {
        model = HttpRepository()
    }

    var itemClickEvent = SingleLiveEvent<BaseRecord>()

    //给ViewPager添加ObservableList
    var items: ObservableList<AIOViewPagerItemViewModel> = ObservableArrayList()

    //给ViewPager添加ItemBinding
    var itemBinding = ItemBinding.of<AIOViewPagerItemViewModel>(BR.viewModel, R.layout.item_viewpager_aio)

    //给ViewPager添加PageTitle
    val pageTitles: PageTitles<AIOViewPagerItemViewModel> = PageTitles { position, item -> "" }

    val tabItemData = ArrayList<TypeResponseBeanData>()

    var tabLoadComplete = SingleLiveEvent<Int>()

    //ViewPager切换监听
    var onPageSelectedCommand = BindingCommand<Int> { index ->
        if (index < items.size) {
            val aioViewPagerItemViewModel = items[index]
            if (aioViewPagerItemViewModel.observableList.size <= 0) {
                aioViewPagerItemViewModel.onRefreshCommand.execute()
            }
        }
    }

    // 请求当前 ViewPager 的数据，结束后数据放到 items 对应ViewModel中
    @SuppressLint("CheckResult")
    fun loadTabsData(moduleCode: String) {
        model.api.queryType(moduleCode)
            .map {
                it.data.add(
                    0, TypeResponseBeanData(
                        "",
                        "null",
                        0,
                        0,
                        "全部",
                        1,
                        ""
                    )
                )
                it
            }
            .compose(RxUtils.schedulersTransformer()) //线程调度
            .doOnSubscribe(this) //请求与ViewModel周期同步
            .doOnSubscribe {}
            .map {
                it as TypeResponseBean
            }.subscribe(
                object : DisposableObserver<TypeResponseBean>() {
                    override fun onNext(responseBean: TypeResponseBean) {
                        // 校验数据
                        if (responseBean.code != "200") {
                            ToastUtils.showShort("请求失败： ${responseBean.code}")
                            return
                        }
                        val list = responseBean.data
                        if (list.isNullOrEmpty()) {
                            ToastUtils.showShort("没有更多数据")
                            return
                        }
                        tabItemData.addAll(list)
                        tabLoadComplete.call()
                    }

                    override fun onError(throwable: Throwable) {
                        if (throwable is ResponseThrowable) {
                            ToastUtils.showShort((throwable as ResponseThrowable).message)
                        }
                    }

                    override fun onComplete() {
                    }
                })
    }

}