package com.goldze.mvvmhabit.aioui.clazz.bean

import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 12:54 下午
 */
data class ClazzResponseBean(
    val code: String,
    val data: ClazzResponseBeanData,
    val message: String,
    val success: Boolean
) : Serializable

data class ClazzResponseBeanData(
    val brief: String,
    val clickCount: Int,
    val courseDescribe: String,
    val createTime: String,
    val deptId: String,
    val faceImage: String,
    val homeImage: String,
    val id: String,
    val isDel: Int,
    val itemList: List<ClazzResponseBeanItem>,
    val name: String,
    val status: Int,
    val updateTime: String
) : Serializable

data class ClazzResponseBeanItem(
    val clickCount: Int,
    val courseId: String,
    val createTime: String,
    val id: String,
    val isDel: Int,
    val name: String,
    val resourcesType: Int,
    val resourcesUrl: String,
    val status: Int,
    val updateTime: String
) : Serializable