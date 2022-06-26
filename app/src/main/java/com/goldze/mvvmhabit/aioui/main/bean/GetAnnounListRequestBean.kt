package com.goldze.mvvmhabit.aioui.main.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/25
 * Time: 11:54 下午
 */
data class GetAnnounListRequestBean(
    val requestBody: GetAnnounListRequestBeanRequestBody,
    val requestHeader: GetAnnounListRequestBeanRequestHeader
)

data class GetAnnounListRequestBeanRequestBody(
    val isDel: Int,
    val pageNum: Int,
    val pageSize: Int,
    val paths: String,
    val status: Int
)

data class GetAnnounListRequestBeanRequestHeader(
    val serialNumber: String,
    val uniqueCode: String
)