package com.goldze.mvvmhabit.aioui.test

import android.app.Application
import android.util.Log
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsRequestBean
import com.google.gson.Gson
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseViewModel

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class TestAModel(application: Application) : BaseViewModel<HttpRepository>(application) {

}