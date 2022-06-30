package com.goldze.mvvmhabit.aioui.test.bean

import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/29
 * Time: 10:37 下午
 */
data class FunnyTestBean(
    val code: String,
    val data: ScaVo,
    val message: String,
    val success: Boolean
) : Serializable
