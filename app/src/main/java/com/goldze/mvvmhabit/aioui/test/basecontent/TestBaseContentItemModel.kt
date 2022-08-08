package com.goldze.mvvmhabit.aioui.test.basecontent

import com.goldze.mvvmhabit.aioui.test.bean.BasicDetailsResponseBeanOption
import com.goldze.mvvmhabit.aioui.test.bean.BasicDetailsResponseBeanQues
import com.goldze.mvvmhabit.aioui.test.bean.Opt
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

class TestBaseContentItemModel(var entity: BasicDetailsResponseBeanOption, var listener: (entity: BasicDetailsResponseBeanOption) -> Unit) {
    var itemClick = BindingCommand<String>(BindingAction {
        listener(entity)
    })
}