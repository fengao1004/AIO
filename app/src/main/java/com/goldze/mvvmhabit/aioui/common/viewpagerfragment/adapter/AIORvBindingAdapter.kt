package com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIORecyclerViewItemViewModel
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

open class AIORvBindingAdapter : BindingRecyclerViewAdapter<AIORecyclerViewItemViewModel>() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: AIORecyclerViewItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
        val entity = item?.entity?.get()
        Log.i(
            "AIORvBindingAdapter",
            "onBindBinding: ${binding.javaClass.simpleName}-${Integer.toHexString(binding.hashCode())} $entity"
        )
    }
}