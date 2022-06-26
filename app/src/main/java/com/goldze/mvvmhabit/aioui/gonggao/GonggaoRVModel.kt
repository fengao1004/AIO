package com.goldze.mvvmhabit.aioui.gonggao

import com.goldze.mvvmhabit.aioui.main.bean.GetAnnounListReponseBeanRecord
import com.goldze.mvvmhabit.aioui.main.bean.GetAnnounListRequestBean
import me.goldze.mvvmhabit.base.ItemViewModel

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

}