package com.goldze.mvvmhabit.aioui.relax.music.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.bean.*
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.bean.list.MusicRecord
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.http.impl.MusicRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.goldze.mvvmhabit.http.ResponseThrowable
import me.goldze.mvvmhabit.utils.RxUtils
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter.PageTitles
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class MusicModel(application: Application) : BaseViewModel<MusicRepository>(application) {
    var playId = ""

    init {
        model = MusicRepository()
    }

    var itemClickEvent = SingleLiveEvent<BaseRecord>()

    var currentTabPosition = 0

    //给ViewPager添加ObservableList
    var items: ObservableList<MusicViewPagerItemViewModel> = ObservableArrayList()

    //给ViewPager添加ItemBinding
    var itemBinding =
        ItemBinding.of<MusicViewPagerItemViewModel>(BR.viewModel, R.layout.item_viewpager_music)

    //给ViewPager添加PageTitle
    val pageTitles: PageTitles<MusicViewPagerItemViewModel> = PageTitles { position, item -> "" }

    //ViewPager切换监听
    var onPageSelectedCommand = BindingCommand<Int> { index ->
        if (index < items.size) {
            currentTabPosition = index
            val pagerItemViewModel = items[index]
            if (pagerItemViewModel.observableList.size <= 0) {
                pagerItemViewModel.onRefreshCommand.execute()
            }
        }
    }

    val tabItemData = ArrayList<TypeResponseBeanData>()

    var tabLoadComplete = SingleLiveEvent<Int>()

    // 请求当前 ViewPager 的数据，结束后数据放到 items 对应ViewModel中
    @SuppressLint("CheckResult")
    fun loadTabsData() {
        model.api.queryType("music")
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

    @SuppressLint("CheckResult")
    fun updateClick(entity: MusicRecord) {
        entity.clickCount = (entity.clickCount ?: 0) + 1
        entity.clickCountOb(entity.clickCount)
        var body = MusicDetailRequestBody()
        body.deptId = entity.deptId ?: 0L
        body.id = entity.id
        body.isDel = entity.isDel
        body.status = entity.status ?: 0
        var request =
            MusicDetailRequest(
                body,
                MusicDetailRequestHeader(Util.serialNumber, Util.uniqueCode)
            )

        model.api.getMusicDetail(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {

            })
    }

}