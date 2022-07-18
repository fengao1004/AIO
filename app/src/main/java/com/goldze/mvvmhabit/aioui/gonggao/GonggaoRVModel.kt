package com.goldze.mvvmhabit.aioui.gonggao

import android.content.Intent
import com.goldze.mvvmhabit.aioui.gonggao.content.GongGaoContentActivity
import com.goldze.mvvmhabit.aioui.main.bean.GetAnnounListReponseBeanRecord
import com.goldze.mvvmhabit.aioui.main.bean.GetAnnounListRequestBean
import me.goldze.mvvmhabit.base.ItemViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 12:19 上午
 */
class GonggaoRVModel(viewModel: GonggaoViewModel, var bean: GetAnnounListReponseBeanRecord) :
    ItemViewModel<GonggaoViewModel>(viewModel) {
    companion object {
        const val clickText = "点击量 "
    }

    var click = BindingCommand<String>(BindingAction {
        var intent = Intent(viewModel.activity, GongGaoContentActivity::class.java)
        intent.putExtra("id", bean.id)
        viewModel.activity.startActivity(intent)
    })
}