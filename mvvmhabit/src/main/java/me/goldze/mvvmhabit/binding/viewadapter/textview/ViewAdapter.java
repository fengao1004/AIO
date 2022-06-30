package me.goldze.mvvmhabit.binding.viewadapter.textview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import me.goldze.mvvmhabit.R;

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

    @BindingAdapter(value = {"marryState", "marryValue"}, requireAll = false)
    public static void setTextBackgroundA(TextView view, final int marryState, final int marryValue) {
        if (marryState == marryValue) {
            view.setBackgroundResource(R.drawable.lib_white_button_blue_stroke);
            view.setTextColor(view.getContext().getResources().getColor(R.color.app_blue));
        } else {
            view.setBackgroundResource(R.drawable.lib_gray_button_buttom);
            view.setTextColor(view.getContext().getResources().getColor(R.color.text_black));
        }
    }

    @BindingAdapter(value = {"sexState", "sexValue"}, requireAll = false)
    public static void setTextBackgroundB(TextView view, final int marryState, final int marryValue) {
        if (marryState == marryValue) {
            view.setBackgroundResource(R.drawable.lib_white_button_blue_stroke);
            view.setTextColor(view.getContext().getResources().getColor(R.color.app_blue));
        } else {
            view.setBackgroundResource(R.drawable.lib_gray_button_buttom);
            view.setTextColor(view.getContext().getResources().getColor(R.color.text_black));
        }
    }

    @BindingAdapter(value = {"testItemIsCheck"}, requireAll = false)
    public static void testItemIsCheck(TextView view, final boolean testItemIsCheck) {
        if (testItemIsCheck) {
            view.setBackgroundResource(R.drawable.lib_white_button_blue_stroke);
            view.setTextColor(view.getContext().getResources().getColor(R.color.app_blue));
        } else {
            view.setBackgroundResource(R.drawable.lib_gray_button_buttom);
            view.setTextColor(view.getContext().getResources().getColor(R.color.text_black));
        }
    }

    @BindingAdapter(value = {"intText"}, requireAll = false)
    public static void testItemIsCheck(TextView view, final int text) {
        view.setText(text + "");
    }


    @BindingAdapter(value = {"stateType", "realState"}, requireAll = false)
    public static void updateState(TextView view, int stateType, final int realState) {
        if (stateType == realState) {
            view.setTextColor(view.getContext().getResources().getColor(R.color.app_blue));
        } else {
            view.setTextColor(Color.parseColor("#BCBCBC"));
        }
    }

    @BindingAdapter(value = {"clazzContentType", "clazzContentValue"}, requireAll = false)
    public static void updateClazzTextState(TextView textView, int clazzContentType, int clazzContentValue) {
        if (clazzContentType == clazzContentValue) {
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.app_blue));
            textView.setTypeface(null, Typeface.BOLD);
        } else {
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.text_gray));
            textView.setTypeface(null, Typeface.NORMAL);
        }
    }

}
