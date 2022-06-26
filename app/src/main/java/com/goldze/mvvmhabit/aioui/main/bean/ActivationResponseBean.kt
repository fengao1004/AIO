package com.goldze.mvvmhabit.aioui.main.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 4:38 下午
 */
data class ActivationResponseBean(
    val code: String,
    val `data`: Data,
    val message: String,
    val success: Boolean
)

data class Data(
    val code: String,
    val `data`: Any,
    val message: String,
    val success: Boolean
)