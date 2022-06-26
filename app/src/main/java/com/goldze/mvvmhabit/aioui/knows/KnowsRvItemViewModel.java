package com.goldze.mvvmhabit.aioui.knows;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;


import com.goldze.mvvmhabit.aioui.knows.content.KnowsContentActivity;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2017/7/17.
 */

public class KnowsRvItemViewModel extends ItemViewModel<KnowsModel> {
    private KnowsBeanRecord left;

    public KnowsBeanRecord getLeft() {
        return left;
    }

    public KnowsBeanRecord getRight() {
        return right;
    }

    public String getRightNum() {
        return rightNum;
    }

    public String getLeftNum() {
        return leftNum;
    }

    private KnowsBeanRecord right;
    public String rightNum;
    public String leftNum;

    public BindingCommand clickLeft = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (left != null) {
                Log.i("fengao_xiaomi", "call: " + (viewModel.activity == null));
                Intent intent = new Intent(viewModel.activity, KnowsContentActivity.class);
                intent.putExtra("bean", left);
                viewModel.activity.startActivity(intent);
            }
        }
    });

    public BindingCommand clickRight = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (right != null) {
                Intent intent = new Intent(viewModel.activity, KnowsContentActivity.class);
                intent.putExtra("bean", right);
                viewModel.activity.startActivity(intent);
            }
        }
    });


    public void setLeft(KnowsBeanRecord left) {
        this.left = left;
        leftNum = "点击量 " + left.getClickCount();
    }

    public void setRight(KnowsBeanRecord right) {
        this.right = right;
        rightNum = "点击量 " + right.getClickCount();
    }

    public KnowsRvItemViewModel(@NonNull KnowsModel viewModel) {
        super(viewModel);
    }
}
