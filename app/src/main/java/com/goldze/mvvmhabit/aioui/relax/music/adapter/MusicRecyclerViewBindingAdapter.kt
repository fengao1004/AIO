package com.goldze.mvvmhabit.aioui.relax.music.adapter

import android.text.TextUtils
import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.bean.list.MusicRecord
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicRvItemViewModel
import com.goldze.mvvmhabit.databinding.ItemRvMusicBinding
import com.goldze.mvvmhabit.utils.ImageUtil
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

class MusicRecyclerViewBindingAdapter : BindingRecyclerViewAdapter<MusicRvItemViewModel>() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: MusicRvItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)

        val record = item?.entity?.get()
        if (binding is ItemRvMusicBinding && record is MusicRecord) {
            if (!TextUtils.isEmpty(record.faceImage)) {
                ImageUtil.display(
                    record.faceImage,
                    binding.coverImage,
                    0
                )
            }
            binding.title.text = record.name
            binding.desc.text = record.musicDescribe
        }
    }
}