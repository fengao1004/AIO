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
data class BasicDetailsResponseBean(
    val code: String,
    val data: BasicDetailsResponseBeanData,
    val message: String,
    val success: Boolean
) : Serializable

data class BasicDetailsResponseBeanData(
    val onceId: String,
    val quesList: List<BasicDetailsResponseBeanQues>
) : Serializable

data class BasicDetailsResponseBeanQues(
    val des: Any,
    val id: Int,
    val options: List<BasicDetailsResponseBeanOption>,
    val title: String,
    val type: Int,
    var times: Int
) : Serializable

data class BasicDetailsResponseBeanOption(
    val id: Int,
    val title: String,
    var isCheck: ObservableBoolean = ObservableBoolean(false)
) : Serializable