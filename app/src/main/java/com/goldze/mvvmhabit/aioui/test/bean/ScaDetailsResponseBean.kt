package com.goldze.mvvmhabit.aioui.test.bean

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/21
 * Time: 10:40 下午
 */
data class ScaDetailsResponseBean(
    val code: String,
    val data: Data,
    val message: String,
    val success: Boolean
) : BaseObservable(), Serializable

data class Data(
    val scaRecId: Long,
    val scaVo: ScaVo
) : BaseObservable(), Serializable

data class ScaVo(
    val brief: String,
    val code: String,
    val status: Int,
    val describe: String,
    val createTime: String,
    val interestDescribe: String,
    val clickCount: Int,
    val duration: Int,
    val faceImage: String,
    val id: String,
    val name: String,
    val quesList: List<Ques>
) : BaseObservable(), Serializable

data class Ques(
    val id: String,
    val interestId: Long,
    val number: String,
    val optList: List<Opt>,
    val sort: Int,
    val isDel: Int,
    val title: String,
    val type: Int,
    var times: Int = 0
) : BaseObservable(), Serializable

data class Opt(
    val id: String,
    val nextQuesId: String,
    val sort: Int,
    val title: String,
    val interestQuesId: Long,
    val result: String,
    var isCheck: ObservableBoolean = ObservableBoolean(false)
) : BaseObservable(), Serializable