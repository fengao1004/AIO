package com.goldze.mvvmhabit.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.io.File


object ImageUtil {
    private const val TAG = "ImageUtil"

    fun pauseRequest(context: Context?) {
        if (context == null) return
        Glide.with(context).pauseRequests()
    }

    fun resumeRequest(context: Context?) {
        if (context == null) return
        Glide.with(context).resumeRequests()
    }

    /**
     * ImageView展示url
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    @JvmOverloads
    fun display(
        url: String?,
        target: ImageView,
        radius: Int = 0,
        placeholderRes: Int = 0,
        errorHolderRes: Int = 0
    ) {
        if (!check(url, target)) {
            return
        }
        val glide = getGlide(target.context, url, radius)
        if (placeholderRes > 0) {
            glide.placeholder(placeholderRes)
        }
        if (errorHolderRes > 0) {
            glide.error(errorHolderRes)
        }
        
        glide.into(target)
    }

    /**
     * ImageView展示url
     */
    fun displayWithSize(
        url: String?,
        target: ImageView,
        radius: Int = 0,
        width: Int = 0,
        height: Int = 0
    ) {
        if (!check(url, target)) {
            return
        }
        displayForImageView(url, target, radius, width, height)
    }

    /**
     * ImageView展示圆角url
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    @JvmOverloads
    fun displayRound(
        url: String?,
        target: ImageView,
        radius: Int = 0,
        placeholderRes: Int = 0,
        errorHolderRes: Int = 0
    ) {
        if (!check(url, target)) {
            return
        }
        val glide = getGlideSafely(target.context, url, radius)
        if (placeholderRes > 0) {
            glide.placeholder(placeholderRes)
        }
        if (errorHolderRes > 0) {
            glide.error(errorHolderRes)
        }
        
        glide.into(target)
    }

    /**
     * ImageView展示圆角url
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    @JvmOverloads
    fun displayRound(
        file: File?,
        target: ImageView,
        radius: Int = 0,
        placeholderRes: Int = 0,
        errorHolderRes: Int = 0
    ) {
        if (!check(file, target)) {
            return
        }
        val glide = getGlideSafely(target.context, file, radius)
        if (placeholderRes > 0) {
            glide.placeholder(placeholderRes)
        }
        if (errorHolderRes > 0) {
            glide.error(errorHolderRes)
        }
        
        glide.into(target)
    }

    /**
     * ImageView展示圆角url
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    @JvmOverloads
    fun displayRound(
        resId: Int,
        target: ImageView,
        radius: Int = 0,
        placeholderRes: Int = 0,
        errorHolderRes: Int = 0
    ) {
        if (!check(resId, target)) {
            return
        }
        val glide = getGlideSafely(target.context, resId, radius)
        if (placeholderRes > 0) {
            glide.placeholder(placeholderRes)
        }
        if (errorHolderRes > 0) {
            glide.error(errorHolderRes)
        }
        
        glide.into(target)
    }

    /**
     * ImageView展示bitmap
     */
    fun display(bitmap: Bitmap?, target: ImageView, radius: Int = 0) {
        if (!check(bitmap, target)) {
            return
        }
        displayForImageView(bitmap, target, radius)
    }

    /**
     * ImageView展示drawable
     */
    fun display(drawable: Drawable?, target: ImageView, radius: Int = 0, width: Int, height: Int) {
        if (!check(drawable, target)) {
            return
        }
        displayForImageView(drawable, target, radius, width, height)
    }

    /**
     * ImageView展示file
     */
    fun display(file: File?, target: ImageView, radius: Int = 0, width: Int, height: Int) {
        if (!check(file, target)) {
            return
        }
        displayForImageView(file, target, radius, width, height)
    }

    /**
     * ImageView展示resId
     */
    fun display(resId: Int, target: ImageView, radius: Int = 0, width: Int, height: Int) {
        if (!check(resId, target)) {
            return
        }
        displayForImageView(resId, target, radius, width, height)
    }

    /**
     * AppWidget展示url
     */
    fun display(
        url: String?,
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        radius: Int = 0
    ) {
        if (!check(url, context, viewId, remoteViews)) {
            return
        }
        displayForAppWidget(url, context, viewId, remoteViews, radius)
    }

    /**
     * AppWidget展示url
     */
    fun displayWithSize(
        url: String?,
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        radius: Int = 0,
        width: Int = 0,
        height: Int = 0,
        opacity: Int = 100
    ) {
        if (!check(url, context, viewId, remoteViews)) {
            return
        }
        displayForAppWidget(url, context, viewId, remoteViews, radius, width, height, opacity)
    }

    /**
     * AppWidget展示bitmap
     */
    fun display(
        bitmap: Bitmap?,
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        radius: Int = 0
    ) {
        if (!check(bitmap, context, viewId, remoteViews)) {
            return
        }
        displayForAppWidget(bitmap, context, viewId, remoteViews, radius)
    }

    /**
     * AppWidget展示drawable
     */
    fun display(
        drawable: Drawable?,
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        radius: Int = 0
    ) {
        if (!check(drawable, context, viewId, remoteViews)) {
            return
        }
        displayForAppWidget(drawable, context, viewId, remoteViews, radius)
    }

    /**
     * AppWidget展示file
     */
    fun display(
        file: File?,
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        radius: Int = 0
    ) {
        if (!check(file, context, viewId, remoteViews)) {
            return
        }
        displayForAppWidget(file, context, viewId, remoteViews, radius)
    }

    /**
     * AppWidget展示resId
     */
    fun display(
        resId: Int,
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        radius: Int = 0,
    ) {
        if (!check(resId, context, viewId, remoteViews)) {
            return
        }
        displayForAppWidget(resId, context, viewId, remoteViews, radius)
    }

    @SuppressLint("CheckResult")
    private fun <T> displayForImageView(
        load: T,
        target: ImageView,
        radius: Int,
        width: Int = 0,
        height: Int = 0
    ) {
        val glide = getGlide(target.context, load, radius, width, height)
        
        glide.into(target)
    }

    private fun <T> displayForAppWidget(
        load: T,
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?,
        radius: Int,
        width: Int = 0,
        height: Int = 0,
        opacity: Int = 100,
    ) {
        var bitmap = getGlide(context, load, radius, width, height)
            .submit()
            .get()
        if (opacity in 0..99) {
            bitmap = alphaBitmap(bitmap, opacity)
        }
        remoteViews!!.setImageViewBitmap(viewId, bitmap)
    }

    fun getBitmap(
        context: Context?,
        url: String,
        radius: Int = 0,
        width: Int = 0,
        height: Int = 0,
        opacity: Int = 100
    ): Bitmap? {
        var bitmap = getGlide(context, url, radius, width, height)
            .submit()
            .get()
        if (opacity in 0..99) {
            bitmap = alphaBitmap(bitmap, opacity)
        }
        return bitmap
    }

    fun alphaBitmap(bitmap: Bitmap, opacity: Int): Bitmap {
        val bitmapWithAlpha =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.alpha = (255.0 * opacity.toDouble() / 100).toInt()
        val canvas = Canvas(bitmapWithAlpha)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return bitmapWithAlpha
    }

    private fun <T> getGlide(context: Context?, load: T, radius: Int): RequestBuilder<Bitmap> {
        var builder = Glide.with(context!!)
            .asBitmap()
            .load(load)
        if (radius > 0) {
            builder = builder.apply(RequestOptions.bitmapTransform(RoundedCorners(radius)))
        }
        return builder
    }

    private fun <T> getGlide(
        context: Context?,
        load: T,
        radius: Int,
        width: Int,
        height: Int
    ): RequestBuilder<Bitmap> {
        var builder = Glide.with(context!!)
            .asBitmap()
            .load(load)
        if (width > 0 && height > 0) {
            builder = builder.apply(RequestOptions().override(width, height))
        }
        if (radius > 0) {
            builder = builder.apply(RequestOptions.bitmapTransform(RoundedCorners(radius)))
        }
        return builder
    }

    /**
     * required Glide instance by corner radius safely check.
     */
    private fun <T> getGlideSafely(
        context: Context?,
        load: T,
        radius: Int
    ): RequestBuilder<Bitmap> {
        return if (radius <= 0) {
            getCenterCropGlide(context, load)
        } else {
            getCenterCropAndRoundCornerGlide(context, load, radius)
        }
    }

    private fun <T> getCenterCropAndRoundCornerGlide(
        context: Context?,
        load: T,
        radius: Int
    ): RequestBuilder<Bitmap> {
        return Glide.with(context!!)
            .asBitmap()
            .load(load)
            .transition(withCrossFade())
            .transform(CenterCrop(), RoundedCorners(radius))
    }

    private fun <T> getCenterCropGlide(context: Context?, load: T): RequestBuilder<Bitmap> {
        return Glide.with(context!!)
            .asBitmap()
            .load(load)
            .transition(withCrossFade())
            .transform(CenterCrop())
    }

    private fun check(path: Any?, target: ImageView?): Boolean {
        if (target == null) {
            Log.w(TAG, "target == null")
            return false
        }
        if (target.context == null) {
            Log.w(TAG, "context == null")
            return false
        }
        if (path == null) {
            Log.w(TAG, "path == null")
            return false
        }
        if ("" == path) {
            Log.w(TAG, "path is empty")
            return false
        }
        return true
    }

    private fun check(
        path: Any?,
        context: Context?,
        viewId: Int,
        remoteViews: RemoteViews?
    ): Boolean {
        if (path == null) {
            Log.w(TAG, "path == null")
            return false
        }
        if ("" == path) {
            Log.w(TAG, "path is empty")
            return false
        }
        if (context == null) {
            Log.w(TAG, "context == null")
            return false
        }
        if (viewId == 0) {
            Log.w(TAG, "viewId is 0")
            return false
        }
        if (remoteViews == null) {
            Log.w(TAG, "remoteViews == null")
            return false
        }
        return true
    }
}
