package com.goldze.mvvmhabit.aioui.zixun.phone

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import me.goldze.mvvmhabit.base.ItemViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand


/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 12:19 上午
 */
class PhoneRVModel(viewModel: PhoneListModel, var bean: PhoneListBeanRecord) :
    ItemViewModel<PhoneListModel>(viewModel) {

    var takePhone = BindingCommand<String>(BindingAction {
        val uri: Uri = Uri.parse("tel:${bean.tel}")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        viewModel.activity.startActivity(intent)
    })

}