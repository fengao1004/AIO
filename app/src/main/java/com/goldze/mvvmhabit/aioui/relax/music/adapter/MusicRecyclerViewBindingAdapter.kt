package com.goldze.mvvmhabit.aioui.relax.music.adapter

import androidx.databinding.ViewDataBinding
import com.goldze.mvvmhabit.aioui.relax.music.viewmodel.MusicRvItemViewModel
import com.goldze.mvvmhabit.databinding.ItemRvMusicBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

class MusicRecyclerViewBindingAdapter :BindingRecyclerViewAdapter<MusicRvItemViewModel>() {

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: MusicRvItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)

        val mBinding = binding as ItemRvMusicBinding

    }
}