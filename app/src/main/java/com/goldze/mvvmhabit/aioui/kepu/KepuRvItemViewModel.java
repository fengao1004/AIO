package com.goldze.mvvmhabit.aioui.kepu;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;


import com.goldze.mvvmhabit.aioui.kepu.content.KepuContentActivity;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2017/7/17.
 */

public class KepuRvItemViewModel extends ItemViewModel<KepuViewModel> {
    public KepuBeanRecord bean;


    public BindingCommand clickLeft = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (bean != null) {
                Log.i("fengao_xiaomi", "call: " + (viewModel.activity == null));
                Intent intent = new Intent(viewModel.activity, KepuContentActivity.class);
                intent.putExtra("bean", bean);
                viewModel.activity.startActivity(intent);
            }
        }
    });


    public KepuRvItemViewModel(@NonNull KepuViewModel viewModel, KepuBeanRecord bean) {
        super(viewModel);
        this.bean = bean;
    }
}
