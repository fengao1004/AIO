package me.goldze.mvvmhabit.binding.viewadapter.textview;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * Created by goldze on 2017/6/16.
 */

public class ViewAdapter {

    /**
     * textView的textStyle是否需要获取焦点
     */
    @BindingAdapter({"typeface"})
    public static void setTextStyle(TextView view, final int typeface) {
        view.setTypeface(null, typeface);
    }

}
