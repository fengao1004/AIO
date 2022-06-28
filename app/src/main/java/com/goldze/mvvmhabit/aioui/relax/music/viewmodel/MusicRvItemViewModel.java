package com.goldze.mvvmhabit.aioui.relax.music.viewmodel;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord;
import com.goldze.mvvmhabit.aioui.bean.list.MusicRecord;
import com.goldze.mvvmhabit.ui.network.detail.DetailFragment;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class MusicRvItemViewModel extends ItemViewModel<MusicViewPagerItemViewModel> {
    public ObservableField<BaseRecord> entity = new ObservableField<>();
    public Drawable musicIcon;
    public Drawable playIcon;

    public MusicRvItemViewModel(@NonNull MusicViewPagerItemViewModel viewModel, BaseRecord entity) {
        super(viewModel);
        this.entity.set(entity);
        //ImageView的占位图片，可以解决RecyclerView中图片错误问题
        musicIcon = ContextCompat.getDrawable(viewModel.getApplication(), R.drawable.player_music_icon_2);
        playIcon = ContextCompat.getDrawable(viewModel.getApplication(), R.drawable.player_start);
    }

    /**
     * 获取position的方式有很多种,indexOf是其中一种，常见的还有在Adapter中、ItemBinding.of回调里
     *
     * @return
     */
    public int getPosition() {
        return viewModel.getItemPosition(this);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            BaseRecord record = entity.get();
            if (record instanceof MusicRecord) {
                ((MusicRecord) record).setItemPosition(getPosition());
            }
            viewModel.onItemClick(record);
        }
    });

    //条目的长按事件
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort(entity.get().getName());
        }
    });
}
