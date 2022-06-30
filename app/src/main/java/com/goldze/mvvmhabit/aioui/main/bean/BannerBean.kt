package com.goldze.mvvmhabit.aioui.main.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 11:49 上午
 */
data class BannerBean(
    val code: String,
    val data: List<BannerBeanData>,
    val message: String,
    val success: Boolean
)

data class BannerBeanData(
    val createTime: String,
    val deptId: String,
    val faceUrl: String,
    val id: String,
    val isDel: Int,
    val linkUrl: String,
    val name: String,
    val resourcesId: String,
    val status: Int,
    val sysModuleCode: String,
    val sysModuleId: Int,
    val systemCode: String,
    val updateTime: String
)