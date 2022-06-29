package com.goldze.mvvmhabit.aioui.zixun.input

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/29
 * Time: 2:48 上午
 */
data class InputRequestBean(
    val requestBody: RequestBody,
    val requestHeader: RequestHeader
)

data class RequestBody(
    val address: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val consultType: String,
    val content: String,
    val isDel: Int,
    val loginName: String,
    val loginPassword: String,
    val pageNum: Int,
    val pageSize: Int,
    val paths: String,
    val questionType: String,
    val status: Int,
    val systemCode: String
)

data class RequestHeader(
    val serialNumber: String,
    val uniqueCode: String
)