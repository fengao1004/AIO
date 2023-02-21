package com.goldze.mvvmhabit.aioui.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/8/20
 * Time: 11:07 上午
 */
data class MusicDetailRequest(
    val requestBody: MusicDetailRequestBody,
    val requestHeader: MusicDetailRequestHeader
)

class MusicDetailRequestBody {
    var deptId: Long = 0
    var id: Long = 0
    var isDel: Int = 0
    var pageNum: Int = 0
    var pageSize: Int = 20
    var paths: String = ""
    var status: Int = 0
    var sysModuleTypeId: Int = 0
    var systemCode: String = ""
    var systemPrefix: String = ""
}

data class MusicDetailRequestHeader(
    val serialNumber: String,
    val uniqueCode: String
)