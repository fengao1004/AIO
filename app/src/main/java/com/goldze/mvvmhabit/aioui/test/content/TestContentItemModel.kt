package com.goldze.mvvmhabit.aioui.test.content

import com.goldze.mvvmhabit.aioui.test.bean.Opt
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

class TestContentItemModel(var entity: Opt, var listener: (entity: Opt) -> Unit) {
    var itemClick = BindingCommand<String>(BindingAction {
        listener(entity)
    })
}