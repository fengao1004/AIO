package com.goldze.mvvmhabit.utils

import android.graphics.Rect
import android.os.SystemClock
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goldze.mvvmhabit.R

/**
 * 触摸事件是否在当前view内
 */
fun View.isMotionEventIn(event: MotionEvent): Boolean {
    val locationInScreen = IntArray(2)
    getLocationOnScreen(locationInScreen)
    val left = locationInScreen[0]
    val right = left + width
    val top = locationInScreen[1]
    val bottom = top + height
    return left <= event.rawX && event.rawX <= right && top <= event.rawY && event.rawY <= bottom
}

/**
 * 防抖动的click监听
 */
fun View.setPreventShakeOnClickListener(diffTime: Int = 1000, clickAction: (Int) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        if (SystemClock.uptimeMillis() - lastClickTime >= diffTime) {
            lastClickTime = SystemClock.uptimeMillis()
            clickAction.invoke(id)
        }
    }
}

/**
 * 扩大view可点击热区
 * @param expandSizeVertical 竖向扩大像素数
 * @param expandSizeHorizontal 横向扩大像素数
 */
fun View.expandClickRect(expandSizeVertical: Int = 40, expandSizeHorizontal: Int = 50) {
    post {
        val delegateArea = Rect()
        getHitRect(delegateArea)
        delegateArea.top -= expandSizeVertical
        delegateArea.bottom += expandSizeVertical
        delegateArea.left -= expandSizeHorizontal
        delegateArea.right += expandSizeHorizontal
        val mParent = parent
        if (mParent is View) {
            mParent.touchDelegate = TouchDelegate(delegateArea, this)
        }
    }
}

/**
 * 按照返回按钮的大小扩展View的点击区域
 */
fun View.expandBackBtnClickRect() {
    if (visibility != View.VISIBLE) {
        return
    }
    val backBtnSize = context.resources.getDimensionPixelSize(R.dimen.brightness_icon)
    post {
        val expandVertical = (backBtnSize - height) / 2
        val expandHorizontal = (backBtnSize - width) / 2
        expandClickRect(
            if (expandVertical <= 0) 0 else expandVertical,
            if (expandHorizontal <= 0) 0 else expandHorizontal
        )
    }
}

/**
 * 控制 View 隐藏的扩展方法
 */
fun View.hide() {
    this.visibility = View.GONE
}

/**
 * 控制 View 展示的扩展方法
 *
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * 深色模式切换时,重新设置adapter进行适配
 */
fun RecyclerView.refreshWhenDayNightModeChange() {
    val layoutManager = layoutManager
    val originalAdapter = adapter
    val currentPosition = if (layoutManager is LinearLayoutManager)
        layoutManager.findFirstCompletelyVisibleItemPosition()
    else -1
    adapter = originalAdapter
    if (currentPosition > -1) {
        scrollToPosition(currentPosition)
    }
}