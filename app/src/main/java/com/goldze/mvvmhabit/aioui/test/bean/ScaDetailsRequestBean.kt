package com.goldze.mvvmhabit.aioui.test.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/25
 * Time: 12:41 下午
 */
data class ScaDetailsRequestBean(
    val param: Param = Param(),
    val scaCode: String
)

data class Param(
    val marry: String = "",
    val sex: String = ""
)