package com.goldze.mvvmhabit.aioui.test

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.Util.delHTMLTag
import com.goldze.mvvmhabit.aioui.bean.list.TestRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIORvBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIORecyclerViewItemViewModel
import com.goldze.mvvmhabit.databinding.ItemRvTestBinding
import com.goldze.mvvmhabit.utils.ImageUtil

class TestARvBindingAdapter : AIORvBindingAdapter() {

    @SuppressLint("SetTextI18n")
    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: AIORecyclerViewItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)

        val record = item?.entity?.get()
        if (binding is ItemRvTestBinding && record is TestRecord) {
            if (!TextUtils.isEmpty(record.faceImage)) {
                ImageUtil.display(
                    record.faceImage,
                    binding.ivImage,
                    0
                )
            }
            binding.tvTitle.text = record.name
            binding.tvContent.text = record.brief?.delHTMLTag()
            binding.clickCount.text = "点击量:" + record.clickCount.toString()
            binding.testCount.text = "题目数量:" + record.quesCount.toString()
        }
    }
}