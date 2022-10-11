package com.goldze.mvvmhabit.aioui.scan.qingxu.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/10/9
 * Time: 10:11 下午
 */
class FaceView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var faceX = 0.0f
    var faceY = 0.0f
    var faceWidth = 0.0f
    var faceHeight = 0.0f
    var paint: Paint = Paint();

    init {
        paint.strokeWidth = 6.0f
        paint.isAntiAlias = true;
        paint.style = Paint.Style.STROKE;
        paint.color = Color.parseColor("#ff0000")
    }

    fun updateFace(x: Int, y: Int, faceWidth: Int, faceHeight: Int, scale: Float) {
        this.faceX = x * scale
        this.faceY = y * scale
        this.faceWidth = faceWidth * scale
        this.faceHeight = faceHeight * scale
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.i("fengao_xiaomi", "onDraw: clear")
        canvas?.drawColor(Color.TRANSPARENT);
        if (faceHeight * faceHeight > 0) {
            Log.i("fengao_xiaomi", "onDraw: $x $y $faceHeight $faceWidth")
            var rect = RectF()
            rect.left = x;
            rect.right = (x + faceWidth);
            rect.top = y;
            rect.bottom = (y + faceHeight);
            canvas?.drawRect(rect, paint);
        }
    }

}