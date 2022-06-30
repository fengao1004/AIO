package com.goldze.mvvmhabit.aioui.knows

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/30
 * Time: 7:31 上午
 */
data class KnowsDetailBean(
    val code: String,
    val data: KnowsBeanRecord,
    val message: String,
    val success: Boolean
)