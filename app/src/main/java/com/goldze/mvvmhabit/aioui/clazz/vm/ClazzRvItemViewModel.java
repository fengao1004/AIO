package com.goldze.mvvmhabit.aioui.clazz.vm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.aioui.clazz.bean.ClazzListResponseBeanRecord;
import com.goldze.mvvmhabit.aioui.clazz.content.ClazzContentActivity;
import com.goldze.mvvmhabit.aioui.video.bean.VideoBean;
import com.goldze.mvvmhabit.entity.DemoEntity;
import com.goldze.mvvmhabit.ui.network.detail.DetailFragment;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 */

public class ClazzRvItemViewModel extends ItemViewModel<ClazzViewPagerItemViewModel> {
    private ClazzListResponseBeanRecord left;

    public ClazzListResponseBeanRecord getLeft() {
        return left;
    }

    public ClazzListResponseBeanRecord getRight() {
        return right;
    }

    public String getRightNum() {
        return rightNum;
    }

    public String getLeftNum() {
        return leftNum;
    }

    private ClazzListResponseBeanRecord right;
    public String rightNum;
    public String leftNum;

    public BindingCommand clickLeft = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (left != null) {
                Log.i("fengao_xiaomi", "call: " + (viewModel.activity == null));
                Intent intent = new Intent(viewModel.activity, ClazzContentActivity.class);
                intent.putExtra("bean", left);
                viewModel.activity.startActivity(intent);
            }
        }
    });

    public BindingCommand clickRight = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (right != null) {

//                VideoBean bean = new VideoBean(right.)
                Intent intent = new Intent(viewModel.activity, ClazzContentActivity.class);
                intent.putExtra("bean", right);
                viewModel.activity.startActivity(intent);
            }
        }
    });


    public void setLeft(ClazzListResponseBeanRecord left) {
        this.left = left;
        leftNum = "点击量 " + left.getClickCount();
    }

    public void setRight(ClazzListResponseBeanRecord right) {
        this.right = right;
        rightNum = "点击量 " + right.getClickCount();
    }

    public ClazzRvItemViewModel(@NonNull ClazzViewPagerItemViewModel viewModel) {
        super(viewModel);
    }
}
