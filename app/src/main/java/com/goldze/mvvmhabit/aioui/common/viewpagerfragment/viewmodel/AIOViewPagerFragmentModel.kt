package com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter.PageTitles
import me.tatarka.bindingcollectionadapter2.ItemBinding


class AIOViewPagerFragmentModel(application: Application) : BaseViewModel<HttpRepository>(application) {

    var itemClickEvent = SingleLiveEvent<BaseRecord>()

    //给ViewPager添加ObservableList
    var items: ObservableList<AIOViewPagerItemViewModel> = ObservableArrayList()

    //给ViewPager添加ItemBinding
    var itemBinding = ItemBinding.of<AIOViewPagerItemViewModel>(BR.viewModel, R.layout.item_viewpager_aio)

    //给ViewPager添加PageTitle
    val pageTitles: PageTitles<AIOViewPagerItemViewModel> = PageTitles { position, item -> "" }

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

}