package com.goldze.mvvmhabit.ui.corner;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ShadowLinearLayout extends LinearLayout implements ShadowProvider {
    private ShadowDelegate shadowDelegate;

    public ShadowLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public ShadowLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        shadowDelegate = new ShadowDelegate();
        shadowDelegate.onCreate(context, attrs, this);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (shadowDelegate != null) {
            shadowDelegate.dispatchDraw(canvas);
        }
    }

    @Override
    public float getCornerRadius() {
        return shadowDelegate == null ? 0 : shadowDelegate.getCornerRadius();
    }

    @Override
    public float getStrokeWidth() {
        return shadowDelegate == null ? 0 : shadowDelegate.getStrokeWidth();
    }

    @Override
    public int getStrokeColor() {
        return shadowDelegate == null ? 0 : shadowDelegate.getStrokeColor();
    }

    @Override
    public float getInitialTranslationZ() {
        return shadowDelegate == null ? 0 : shadowDelegate.getInitialTranslationZ();
    }
}
