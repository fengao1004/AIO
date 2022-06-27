package com.goldze.mvvmhabit.aioui.relax.film

import android.text.TextUtils
import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.bean.list.FilmRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIORvBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIORecyclerViewItemViewModel
import com.goldze.mvvmhabit.databinding.ItemRvFilmBinding
import com.goldze.mvvmhabit.utils.ImageUtil

class FilmRvBindingAdapter : AIORvBindingAdapter() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: AIORecyclerViewItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)

        val record = item?.entity?.get()
        if (binding is ItemRvFilmBinding && record is FilmRecord) {
            if (!TextUtils.isEmpty(record.faceImage)) {
                ImageUtil.display(
                    record.faceImage,
                    binding.cover,
                    0
                )
            }
            binding.title.text = record.name
            binding.clickCount.text = record.clickCount.toString()

        }

    }
}