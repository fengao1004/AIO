package com.goldze.mvvmhabit.aioui.http

import com.goldze.mvvmhabit.utils.RetrofitClient
import me.goldze.mvvmhabit.base.BaseModel

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:57 下午
 */
open class HttpRepository : BaseModel() {
    companion object {
        const val isTest = false
        const val H5_URL = "http://11.client.yixinxd.com/psyClientWeb/index.html#" //正式环境
        const val TEST_H5_URL = "http://11.sit.client.yixinxd.com/psyClientWeb/#" //测试环境
        const val QX_H5_URL = "http://11.client.yixinxd.com/report/index.html#" //测试环境
        const val QX_TEST_H5_URL = "http://11.sit.client.yixinxd.com/report/index.html#" //测试环境


        fun getH5base():String{
            return if (isTest) {
                TEST_H5_URL
            } else {
                H5_URL
            }
        }

        fun getQXH5base():String{
            return if (isTest) {
                QX_TEST_H5_URL
            } else {
                QX_H5_URL
            }
        }
    }

    var api = RetrofitClient.getInstance().create(Api::class.java)
}