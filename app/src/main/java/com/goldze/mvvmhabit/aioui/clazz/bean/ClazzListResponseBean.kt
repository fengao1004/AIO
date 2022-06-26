package com.goldze.mvvmhabit.aioui.clazz.bean

import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 12:54 下午
 */
data class ClazzListResponseBean(
    val code: String,
    val data: ClazzListResponseBeanData,
    val message: String,
    val success: Boolean
)

data class ClazzListResponseBeanData(
    val current: Int,
    val pages: Int,
    val records: List<ClazzListResponseBeanRecord>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int
)

data class ClazzListResponseBeanRecord(
    val brief: String,
    val clickCount: Int,
    val courseDescribe: String,
    val createTime: String,
    val deptId: Int,
    val faceImage: String,
    val homeImage: String,
    val id: Int,
    val isDel: Int,
    val name: String,
    val status: Int,
    val updateTime: String
) : Serializable