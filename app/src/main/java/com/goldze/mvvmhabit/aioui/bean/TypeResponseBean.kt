package com.goldze.mvvmhabit.aioui.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 11:36 上午
 */
data class TypeResponseBean(
    val code: String,
    val `data`: List<TypeResponseBeanData>,
    val message: String,
    val success: Boolean
)

data class TypeResponseBeanData(
    val createTime: String,
    val id: String,
    val isDel: Int,
    val moduleId: Int,
    val name: String,
    val status: Int,
    val updateTime: String
)