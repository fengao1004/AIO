package com.goldze.mvvmhabit.aioui.relax.meditation

import android.text.TextUtils
import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.bean.list.MeditationRecord
import com.goldze.mvvmhabit.aioui.bean.list.MusicRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIORvBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIORecyclerViewItemViewModel
import com.goldze.mvvmhabit.databinding.ItemRvMeditationBinding
import com.goldze.mvvmhabit.utils.ImageUtil

class MeditationRvBindingAdapter : AIORvBindingAdapter() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: AIORecyclerViewItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)

        val record = item?.entity?.get()
        if (binding is ItemRvMeditationBinding && record is MeditationRecord) {
            if (!TextUtils.isEmpty(record.faceImage)) {
                ImageUtil.display(
                    record.faceImage,
                    binding.coverImage,
                    0
                )
            }
            binding.title.text = record.name
            binding.desc.text = record.brief
        }
    }
}