package com.goldze.mvvmhabit.aioui.http

import com.goldze.mvvmhabit.utils.RetrofitClient
import me.goldze.mvvmhabit.base.BaseModel

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:57 下午
 */
class HttpRepository: BaseModel() {
    var api = RetrofitClient.getInstance().create(Api::class.java)
}