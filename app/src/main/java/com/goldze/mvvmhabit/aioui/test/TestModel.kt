package com.goldze.mvvmhabit.aioui.test

import android.app.Application
import android.graphics.Typeface
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.ui.network.NetWorkItemViewModel
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class TestModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var isSpecialty:MutableLiveData<Boolean> = MutableLiveData()
    var specialtyTagVisible = ObservableBoolean(true)
    var specialtyTagStyle = ObservableInt(Typeface.BOLD)
    var normalTagVisible = ObservableBoolean(false)
    var normalTagStyle = ObservableInt(Typeface.NORMAL)

    var specialtyTClick = BindingCommand<String>(BindingAction {
        isSpecialty.value = true
        specialtyTagVisible.set(true)
        specialtyTagStyle.set(Typeface.BOLD)
        normalTagVisible.set(false)
        normalTagStyle.set(Typeface.NORMAL)
    })

    var normalClick = BindingCommand<String>(BindingAction {
        isSpecialty.value = false
        specialtyTagVisible.set(false)
        specialtyTagStyle.set(Typeface.NORMAL)
        normalTagVisible.set(true)
        normalTagStyle.set(Typeface.BOLD)
    })


    //给RecyclerView添加ObservableList
    var observableList: ObservableList<TestListModel> = ObservableArrayList()

    //RecyclerView多布局添加ItemBinding
    var itemBinding = ItemBinding.of<TestListModel>(BR.vm, R.layout.item_test)

    init {
        observableList.add(TestListModel(TestBean()))
        observableList.add(TestListModel(TestBean()))
        observableList.add(TestListModel(TestBean()))
        observableList.add(TestListModel(TestBean()))
        observableList.add(TestListModel(TestBean()))
        observableList.add(TestListModel(TestBean()))
        isSpecialty.value = true
    }

}