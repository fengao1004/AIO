package com.goldze.mvvmhabit.aioui.zixun.phone

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/29
 * Time: 12:25 上午
 */
data class PhoneListBean(
    val code: String,
    val data: PhoneListBeanData,
    val message: String,
    val success: Boolean
)

data class PhoneListBeanData(
    val current: Int,
    val pages: Int,
    val records: List<PhoneListBeanRecord>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int
)

data class PhoneListBeanRecord(
    val createTime: String,
    val deptId: Int,
    val id: Int,
    val isDel: Int,
    val name: String,
    val status: Int,
    val tel: String,
    val updateTime: String,
    val workTime: String
)