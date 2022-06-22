package com.goldze.mvvmhabit.aioui.test.content

import android.app.Application
import androidx.databinding.ObservableArrayList
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.test.TestListModel
import me.goldze.mvvmhabit.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class TestContentModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var observableList = ObservableArrayList<TestContentItemModel>()
    var itemBinding = ItemBinding.of<TestContentItemModel>(BR.viewModel, R.layout.item_test)

    init {
        observableList.add(TestContentItemModel(TestItem()))
        observableList.add(TestContentItemModel(TestItem()))
        observableList.add(TestContentItemModel(TestItem()))
        observableList.add(TestContentItemModel(TestItem()))
        observableList.add(TestContentItemModel(TestItem()))
        observableList.add(TestContentItemModel(TestItem()))
    }
}

data class TestItem(
    var text: String = "",
    var isCheck: Boolean = false
)