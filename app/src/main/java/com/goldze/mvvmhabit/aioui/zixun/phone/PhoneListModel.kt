package com.goldze.mvvmhabit.aioui.zixun.phone

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.gonggao.GonggaoRVModel
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
class PhoneListModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    init {
        model = HttpRepository()
    }

    //给RecyclerView添加ObservableList
    var observableList: ObservableList<PhoneRVModel> = ObservableArrayList()

    //给RecyclerView添加ItemBinding
    var itemBinding = ItemBinding.of<PhoneRVModel>(BR.viewModel, R.layout.item_phone_rv)

    @SuppressLint("CheckResult")
    fun loadData() {
        model.api.getPhoneList(
            CommentRequestBean(
                CommentRequestBean.DEFAULT,
                CommentRequestBean.getHeader()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    observableList.clear()
                    it.data.records.forEach {
                        observableList.add(PhoneRVModel(this, it))
                    }
                } else {
                    Log.i("fengao_xiaomi", "loadData: ")
                }
            }, {
                Log.i("fengao_xiaomi", "loadData: $it")
            })
    }
}