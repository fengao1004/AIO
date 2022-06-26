package com.goldze.mvvmhabit.aioui.http

import com.goldze.mvvmhabit.utils.RetrofitClient
import me.goldze.mvvmhabit.base.BaseModel

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:57 下午
 */
open class HttpRepository: BaseModel() {
    companion object{
        const val BASE_H5_URL = "http://11.sit.client.yixinxd.com/psyClientWeb/#/" //测试环境
//        const val BASE_H5_URL = "http://11.sit.client.yixinxd.com/psyClientWeb/#/" // 正式环境
    }
    var api = RetrofitClient.getInstance().create(Api::class.java)
}