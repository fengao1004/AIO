package com.goldze.mvvmhabit.aioui.knows

import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 5:27 下午
 */
data class KnowsBean(
    val code: String,
    val data: KnowsBeanData,
    val message: String,
    val success: Boolean
)

data class KnowsBeanData(
    val current: String,
    val orders: List<Any>,
    val pages: String,
    val records: List<KnowsBeanRecord>,
    val searchCount: Boolean,
    val size: String,
    val total: String
)

data class KnowsBeanRecord(
    val brief: String,
    val clickCount: Int,
    val createTime: String,
    val deptId: String,
    val faceImage: String,
    val homeImage: String,
    val id: String,
    val infoDescribe: String,
    val isDel: Int,
    val name: String,
    val status: Int,
    val updateTime: String
):Serializable