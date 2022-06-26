package com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2017/7/17.
 */

public class AIORecyclerViewItemViewModel extends ItemViewModel<AIOViewPagerItemViewModel> {

    public ObservableField<BaseRecord> entity = new ObservableField<>();


    public AIORecyclerViewItemViewModel(@NonNull AIOViewPagerItemViewModel viewModel, BaseRecord entity) {
        super(viewModel);
        this.entity.set(entity);
        // 绑定数据
    }


    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            String content = "<image src=\"http://39.103.226.17/psy/default/01焦虑测验GAD-7.jpg\" style=\"width: 100%\"/><image src=\"http://39.103.226.17/psy/default/01焦虑测验GAD-7.jpg\" style=\"width: 100%\"/><image src=\"http://39.103.226.17/psy/default/01焦虑测验GAD-7.jpg\" style=\"width: 100%\"/>";
            entity.get().setContent(content);
            viewModel.onItemClick(entity.get());
        }
    });

    //条目的长按事件
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
}
