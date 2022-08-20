package com.goldze.mvvmhabit.aioui.clazz.fragment

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.databinding.Observable
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.bean.list.TestRecord
import com.goldze.mvvmhabit.aioui.clazz.bean.ClazzListResponseBeanRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIORvBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIORecyclerViewItemViewModel
import com.goldze.mvvmhabit.databinding.ItemClazzRvBinding
import com.goldze.mvvmhabit.databinding.ItemRvTestBinding
import com.goldze.mvvmhabit.utils.ImageUtil

class ClazzRvBindingAdapter : AIORvBindingAdapter() {

    @SuppressLint("SetTextI18n")
    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: AIORecyclerViewItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
        var record = item?.entity?.get()

        if (binding is ItemClazzRvBinding && record is ClazzListResponseBeanRecord) {
            binding.ivLeft
            if (!TextUtils.isEmpty(record.faceImage)) {
                ImageUtil.display(
                    record.faceImage,
                    binding.ivLeft,
                    0
                )
            }
            binding.tvName.text = record.name
            binding.tvNum.text = "点击量："+record.clickCount
            record.clickCountOb = {
                binding.tvNum.text = "点击量：" + record.clickCount
            }
        }
    }
}