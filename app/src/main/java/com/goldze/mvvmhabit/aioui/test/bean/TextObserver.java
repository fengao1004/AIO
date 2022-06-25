package com.goldze.mvvmhabit.aioui.test.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.goldze.mvvmhabit.BR;

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/25
 * Time: 8:42 下午
 */
public class TextObserver extends BaseObservable {
    public String value;

    public TextObserver(String value) {
        this.value = value;
    }

    @Bindable
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        notifyPropertyChanged(BR.value);   //注意：未转换布局BR是引用不到price的
    }
}