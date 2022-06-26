package com.goldze.mvvmhabit.aioui.main.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 6:41 下午
 */
data class ShebeiXQBean(
    val code: String,
    val data: ShebeiXQBeanData,
    val message: String,
    val success: Boolean
)

data class ShebeiXQBeanData(
    val code: String,
    val data: ShebeiXQBeanDataX,
    val message: String,
    val success: Boolean
)

data class ShebeiXQBeanDataX(
    val address: String,
    val backColour: String,
    val code: String,
    val createTime: String,
    val deptId: String,
    val deptIntroduce: String,
    val id: String,
    val isActivation: Int,
    val isCanAppointment: Int,
    val isCanScan: Int,
    val isDel: Int,
    val logo: String,
    val name: String,
    val serialNumber: String,
    val status: Int,
    val uniqueCode: String,
    val updateTime: String
)