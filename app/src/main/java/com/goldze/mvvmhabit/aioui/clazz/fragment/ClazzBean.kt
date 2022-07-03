package com.goldze.mvvmhabit.aioui.clazz.fragment

import com.goldze.mvvmhabit.aioui.bean.list.BaseRecord
import com.goldze.mvvmhabit.aioui.test.bean.ScaVo
import java.io.Serializable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/7/3
 * Time: 8:45 下午
 */
class ClazzBean(
    val code: String,
    val data: ClazzRecord,
    val message: String,
    val success: Boolean
) : Serializable


class ClazzRecord: BaseRecord() {

}