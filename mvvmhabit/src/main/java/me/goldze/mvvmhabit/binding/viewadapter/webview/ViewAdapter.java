package me.goldze.mvvmhabit.binding.viewadapter.webview;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import androidx.databinding.BindingAdapter;

/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    @BindingAdapter({"render"})
    public static void loadHtml(WebView webView, final String html) {
        if (!TextUtils.isEmpty(html)) {
            Log.i("fengao_xiaomi", "loadHtml: "+html);
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }
}
