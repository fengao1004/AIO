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
        var DEFAULT = RequestBody(0, 0, 0, 100, "", 1, "null", "")
        
        fun getEmpty(): RequestBody {
            return RequestBody(0, 0, 0, 100, "", 1, "null", "")
        }

        fun getHeader(): RequestHeader {
            return RequestHeader(Util.serialNumber, Util.uniqueCode)
        }
    }


}

data class RequestBody(
    var isDel: Int,
    var id: Long,
    var pageNum: Int,
    var pageSize: Int,
    var paths: String,
    var status: Int,
    var sysModuleTypeId: String,
    var systemPrefix: String,
)

data class RequestHeader(
    var serialNumber: String,
    var uniqueCode: String
)