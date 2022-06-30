package me.goldze.mvvmhabit.binding.viewadapter.image;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"garyRes", "checkRes", "stateType", "realState"}, requireAll = false)
    public static void updateState(ImageView imageView, int garyRes, int checkRes, int stateType, int realState) {
        if (stateType == realState) {
            imageView.setImageResource(checkRes);
        } else {
            imageView.setImageResource(garyRes);
        }
    }

    @BindingAdapter(value = {"clazzContentType", "clazzContentValue"}, requireAll = false)
    public static void updateClazzImageState(ImageView imageView, int clazzContentType, int clazzContentValue) {
        if (clazzContentType == clazzContentValue) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}

