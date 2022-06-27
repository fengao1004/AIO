package com.goldze.mvvmhabit.aioui.relax.music.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.http.impl.MusicRepository
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter.PageTitles
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class MusicModel(application: Application) : BaseViewModel<MusicRepository>(application) {

    var itemClickEvent = SingleLiveEvent<BaseRecord>()

    //给ViewPager添加ObservableList
    var items: ObservableList<MusicViewPagerItemViewModel> = ObservableArrayList()

    //给ViewPager添加ItemBinding
    var itemBinding = ItemBinding.of<MusicViewPagerItemViewModel>(BR.viewModel, R.layout.item_viewpager_music)

    //给ViewPager添加PageTitle
    val pageTitles: PageTitles<MusicViewPagerItemViewModel> = PageTitles { position, item -> "" }

    //ViewPager切换监听
    var onPageSelectedCommand = BindingCommand<Int> { index ->
        if (index < items.size) {
            val pagerItemViewModel = items[index]
            if (pagerItemViewModel.observableList.size <= 0) {
                pagerItemViewModel.onRefreshCommand.execute()
            }
        }
    }

    // 请求当前 ViewPager 的数据，结束后数据放到 items 对应ViewModel中

}