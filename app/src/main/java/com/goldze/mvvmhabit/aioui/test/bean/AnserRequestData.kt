package com.goldze.mvvmhabit.aioui.test.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/25
 * Time: 10:43 下午
 */
data class AnserRequestData(
    val param: AnserRequestDataParam,
    val quesList: List<AnserRequestDataQues>,
    val scaRecId: Long
)

data class AnserRequestDataParam(
    val marry: String,
    val sex: String
)

data class AnserRequestDataQues(
    val ext1: String,
    val optIdList: List<String>,
    val quesId: Int,
    val scaRecId: Long,
    val times: Int
)