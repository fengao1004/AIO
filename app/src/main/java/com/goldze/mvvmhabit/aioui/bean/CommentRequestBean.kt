package com.goldze.mvvmhabit.aioui.bean

import com.goldze.mvvmhabit.aioui.Util

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 11:33 上午
 */
data class CommentRequestBean(
    var requestBody: RequestBody,
    var requestHeader: RequestHeader
) {
    companion object {
        var DEFAULT = RequestBody(0, "0", 0, 20, "", 1, "null", "", 0, "","")

        fun getEmpty(): RequestBody {
            return RequestBody(0, "0", 0, 20, "", 1, "null", "", 0, "","")
        }

        fun getHeader(): RequestHeader {
            return RequestHeader(Util.serialNumber, Util.uniqueCode)
        }
    }
}

data class RequestBody(
    var isDel: Int,
    var id: String?,
    var pageNum: Int,
    var pageSize: Int,
    var paths: String,
    var status: Int,
    var sysModuleTypeId: String,
    var systemPrefix: String,
    var themeId: Long,
    var typeId: String,
    var sysModuleId:String
)

data class RequestHeader(
    var serialNumber: String,
    var uniqueCode: String
)