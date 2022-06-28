package com.goldze.mvvmhabit.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

public class JinScreenUtil {
    public static void getScreenInfo(Activity activity) {
        // 获取屏幕分辨率
        int screenWidth;        // 屏幕宽
        int screenHeight;        // 屏幕高

        // 获取像素密度和屏幕密度
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();

        float density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI;        // 像素密度（每寸像素：120/160/240/320）
        float xdpi;               //X轴方向的像素密度
        float ydpi;                //Y轴方向的像素密度

        screenWidth = dm.widthPixels;        // 屏幕宽
        screenHeight = dm.heightPixels;        // 屏幕高

        Log.e("屏幕分辨率", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);

        // 获取屏幕密度（方法3）
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        density = dm.density;
        densityDPI = dm.densityDpi;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;

        Log.e("屏幕XY轴方向上的像素密度", "xdpi=" + xdpi + "; ydpi=" + ydpi);
        Log.e("屏幕像素密度和屏幕密度", "density=" + density + "; densityDPI=" + densityDPI);
    }
}
