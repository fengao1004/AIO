package com.goldze.mvvmhabit.ui.corner;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;


public class ViewRadiusOutline extends ViewOutlineProvider {
    private float mRadius;

    public ViewRadiusOutline(float radius) {
        mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), mRadius);
    }
}
