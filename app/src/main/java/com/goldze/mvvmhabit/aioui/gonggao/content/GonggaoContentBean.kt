package com.goldze.mvvmhabit.aioui.gonggao.content

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/7/13
 * Time: 11:11 下午
 */
data class GonggaoContentBean(
    val code: String,
    val data: GonggaoContentBeanData,
    val message: String,
    val success: Boolean
)

data class GonggaoContentBeanData(
    val clickCount: Int,
    val content: String,
    val createTime: String,
    val deptId: Int,
    val id: Long,
    val isDel: Int,
    val name: String,
    val status: Int,
    val updateTime: String
)