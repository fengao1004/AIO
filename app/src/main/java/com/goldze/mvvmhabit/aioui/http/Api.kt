package com.goldze.mvvmhabit.aioui.http

import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsRequestBean
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:59 下午
 */
interface Api {
    @POST("/client/scaRec/notLogin/create")
    fun getScaDetails(@Body bean: ScaDetailsRequestBean): Observable<ScaDetailsResponseBean>
}