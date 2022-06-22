package com.goldze.mvvmhabit.aioui.test.bean

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/21
 * Time: 10:40 下午
 */
data class TestContentBean(
    val code: String,
    val data: Data,
    val message: String,
    val success: Boolean
)

data class Data(
    val scaRecId: String,
    val scaVo: ScaVo
)

data class ScaVo(
    val brief: String,
    val code: String,
    val describe: String,
    val duration: Int,
    val faceImage: String,
    val id: String,
    val name: String,
    val quesList: List<Ques>
)

data class Ques(
    val id: String,
    val number: String,
    val optList: List<Opt>,
    val sort: Int,
    val title: String,
    val type: Int
)

data class Opt(
    val id: String,
    val nextQuesId: String,
    val sort: Int,
    val title: String
)