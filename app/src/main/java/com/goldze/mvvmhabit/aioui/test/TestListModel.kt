package com.goldze.mvvmhabit.aioui.test

import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/21
 * Time: 12:57 上午
 */
class TestListModel(var entity: TestBean) {
    var itemClick = BindingCommand<String>(BindingAction {

    })


}