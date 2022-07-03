package com.goldze.mvvmhabit.aioui.clazz.bean

import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/26
 * Time: 12:54 下午
 */
data class ClazzListResponseBeanRecord(
    val courseDescribe: String,
    val faceImage: String,
    val homeImage: String,
) : BaseRecord(), Serializable