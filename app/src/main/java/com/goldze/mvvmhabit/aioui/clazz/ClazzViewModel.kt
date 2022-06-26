package com.goldze.mvvmhabit.aioui.clazz

import android.annotation.SuppressLint
import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.clazz.vm.ClazzViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class ClazzViewModel(application: Application) : BaseViewModel<HttpRepository>(application) {

    var itemClickEvent = SingleLiveEvent<String>()

    var lifecycleOwner: LifecycleOwner? = null

    //给ViewPager添加ObservableList
    var items: ObservableList<ClazzViewPagerItemViewModel> = ObservableArrayList()

    //给ViewPager添加ItemBinding
    var itemBinding =
        ItemBinding.of<ClazzViewPagerItemViewModel>(BR.viewModel, R.layout.item_viewpager_clazz)

    //给ViewPager添加PageTitle
    val pageTitles: BindingViewPagerAdapter.PageTitles<ClazzViewPagerItemViewModel> =
        BindingViewPagerAdapter.PageTitles { position, item -> "${item.text}" }

    //ViewPager切换监听
    var onPageSelectedCommand = BindingCommand<Int> { index ->

    }

    init {
        model = HttpRepository()

    }

    @SuppressLint("CheckResult")
    fun loadData() {
        model.api.queryType("course")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    items.clear()
                    it.data.forEach {
                        var vm = ClazzViewPagerItemViewModel(getApplication(), it)
                        vm.activity = activity
                        items.add(vm)
                    }
                } else {
                    ToastUtils.showShort("数据加载错误 7")
                }
            }, {
                ToastUtils.showShort("数据加载错误 6")
            })
    }

}