package com.goldze.mvvmhabit.aioui.kepu

import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 5:27 下午
 */
data class KepuBean(
    val code: String,
    val data: KepuBeanData,
    val message: String,
    val success: Boolean
)

data class KepuBeanData(
    val current: String,
    val orders: List<String>,
    val pages: String,
    val records: List<KepuBeanRecord>,
    val searchCount: Boolean,
    val size: String,
    val total: String
)

data class KepuBeanRecord(
    val brief: String,
    val createTime: String,
    val deptId: String,
    val faceImage: String,
    val homeImage: String,
    val id: String,
    val isDel: Int,
    val name: String,
    val status: Int,
    val updateTime: String
):Serializable