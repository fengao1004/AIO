package com.goldze.mvvmhabit.aioui.main.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/25
 * Time: 11:57 下午
 */
data class GetAnnounListReponseBean(
    val code: String,
    val data: GetAnnounListReponseBeanData,
    val message: String,
    val success: Boolean
)

data class GetAnnounListReponseBeanData(
    val current: String,
    val orders: List<Any>,
    val pages: String,
    val records: List<GetAnnounListReponseBeanRecord>,
    val searchCount: Boolean,
    val size: String,
    val total: String
)

data class GetAnnounListReponseBeanRecord(
    val content: String,
    val createTime: String,
    val deptId: String,
    val id: String,
    val isDel: Int,
    val clickCount: Int,
    val name: String,
    val status: Int,
    val updateTime: String,
    val clickNum: String = "5"
)