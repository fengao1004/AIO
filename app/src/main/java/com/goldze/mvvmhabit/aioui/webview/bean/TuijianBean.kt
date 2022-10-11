package com.goldze.mvvmhabit.aioui.webview.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/10/9
 * Time: 7:11 下午
 */
data class TuijianBean(
    val code: String,
    val data: List<TuijianBeanData>,
    val message: String,
    val success: Boolean
)

data class TuijianBeanData(
    val clickCount: Int,
    val faceImage: String,
    val id: Long,
    val moduleCode: String,
    val name: String,
    val sysTagId: Long
)


