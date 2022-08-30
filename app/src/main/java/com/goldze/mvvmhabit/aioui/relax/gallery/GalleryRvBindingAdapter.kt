package com.goldze.mvvmhabit.aioui.relax.gallery

import android.text.TextUtils
import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.bean.list.CartoonRecord
import com.goldze.mvvmhabit.aioui.bean.list.MeditationRecord
import com.goldze.mvvmhabit.aioui.bean.list.MusicRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIORvBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIORecyclerViewItemViewModel
import com.goldze.mvvmhabit.databinding.ItemRvGalleryBinding
import com.goldze.mvvmhabit.databinding.ItemRvMeditationBinding
import com.goldze.mvvmhabit.utils.ImageUtil

class GalleryRvBindingAdapter : AIORvBindingAdapter() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: AIORecyclerViewItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)

        val record = item?.entity?.get()
        if (binding is ItemRvGalleryBinding && record is CartoonRecord) {
            if (!TextUtils.isEmpty(record.faceImage)) {
                ImageUtil.display(
                    record.faceImage,
                    binding.cover,
                    0
                )
            }
            binding.title.text = record.name
            binding.tvNum.text = "点击量：" + record.clickCount
            record.clickCountOb = {
                binding.tvNum.text = "点击量：" + record.clickCount
            }
        }

    }
}