package com.goldze.mvvmhabit.aioui.test.bean

import com.google.gson.JsonObject

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/25
 * Time: 10:45 下午
 */
data class AnserReponseData(
    val code: String,
    val data: AnserReponseDataData,
    val message: String,
    val success: Boolean
)

data class AnserReponseDataData(
    val constraintResult: JsonObject,
    val scaRecId: Long
)
