package com.goldze.mvvmhabit.ui.corner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.goldze.mvvmhabit.R

class ShadowDelegate : ShadowProvider {
    private val _invalidRes = -1f

    private var mCornerRadius = 0f
    private var mStrokeWidth = 0f
    private var mStrokeColor = 0
    private var mInitialTranslationZ = 0f

    private var mStrokePaint: Paint? = null

    fun onCreate(context: Context, attrs: AttributeSet, renderView: View) {
        parseAttributeSet(context, attrs)
        setupOutlineProvider(renderView)
        setupStrokePaint()

        mInitialTranslationZ = renderView.translationZ
    }

    private fun parseAttributeSet(context: Context, attrs: AttributeSet?) {
        val attrsSet = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)
        mCornerRadius = attrsSet.getDimension(
            R.styleable.ShadowLayout_sl_cornerRadius,
            _invalidRes
        )
        mStrokeWidth = attrsSet.getDimension(
            R.styleable.ShadowLayout_sl_strokeWidth,
            _invalidRes
        )
        mStrokeColor = attrsSet.getColor(R.styleable.ShadowLayout_sl_strokeColor, Color.TRANSPARENT)
        attrsSet.recycle()
    }

    private fun setupStrokePaint() {
        if (mStrokeWidth <= 0 || mStrokeColor == Color.TRANSPARENT) return

        mStrokePaint = Paint()
        mStrokePaint!!.isAntiAlias = true
        mStrokePaint!!.isDither = true
        mStrokePaint!!.style = Paint.Style.STROKE
        mStrokePaint!!.strokeWidth = mStrokeWidth
        mStrokePaint!!.color = mStrokeColor
    }

    private fun setupOutlineProvider(renderView: View?) {
        if (mCornerRadius > 0) {
            renderView?.outlineProvider = ViewRadiusOutline(mCornerRadius)
            renderView?.clipToOutline = true
        }
    }

    fun dispatchDraw(canvas: Canvas) {
        if (mStrokePaint == null) return

        val width = canvas.width
        val height = canvas.height
        val left = 0
        val top = 0

        if (mCornerRadius <= 0) {
            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                width.toFloat(),
                height.toFloat(),
                mStrokePaint!!
            )
        } else {
            canvas.drawRoundRect(
                left.toFloat(),
                top.toFloat(),
                width.toFloat(),
                height.toFloat(),
                mCornerRadius,
                mCornerRadius,
                mStrokePaint!!
            )
        }
    }

    override fun getCornerRadius() = mCornerRadius

    override fun getStrokeWidth() = mStrokeWidth

    override fun getStrokeColor() = mStrokeColor

    override fun getInitialTranslationZ() = mInitialTranslationZ
}