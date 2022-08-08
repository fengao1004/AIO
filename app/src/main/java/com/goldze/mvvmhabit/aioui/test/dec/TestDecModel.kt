package com.goldze.mvvmhabit.aioui.test.dec

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.TextObserver
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.test.basecontent.TestBaseContentActivity
import com.goldze.mvvmhabit.aioui.test.bean.BasicDetailsResponseBeanData
import com.goldze.mvvmhabit.aioui.test.bean.FunnyTestBean
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsRequestBean
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import com.goldze.mvvmhabit.aioui.test.content.TestContentActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class TestDecModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var detail: ScaDetailsResponseBean? = null
    var detail2: FunnyTestBean? = null
    var type = ""
    var onceId = ""
    var brief = TextObserver("")
    var imgUrl = TextObserver("")
    var name = TextObserver("")
    var scaCode = ""
    var basicBean: BasicDetailsResponseBeanData? = null

    //    var detail: ScaDetailsResponseBean? = null
    init {
        model = HttpRepository()
    }

    var jumpBasic = false

    @SuppressLint("CheckResult")
    fun loadData(code: String?, name: String?, type: String?) {
        this.type = type!!
        if (type == "normal") {
            scaCode = code!!
            model.api.getScaBasics(scaCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.success && it.data.quesList.isNotEmpty()) {
                        jumpBasic = true
                        basicBean = it.data
                        onceId = it.data.onceId
                    }
                    model.api.getScaDetails(
                        ScaDetailsRequestBean(
                            scaCode = code!!,
                            onceId = onceId
                        )
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.success) {
                                Log.i("fengao_xiaomi", "loadData: " + Gson().toJson(it))
                                detail = it
                                brief.value = detail!!.data.scaVo.brief
                                this.name.value = detail!!.data.scaVo.name
                                imgUrl.value = detail!!.data.scaVo.faceImage
                            }
                        }, {
                            it.printStackTrace()
                            Log.i("fengao_xiaomi", "loadData: ${it.message}")
                        })
                }, {
                    it.printStackTrace()
                    Log.i("fengao_xiaomi", "loadData: ${it.message}")
                    ToastUtils.showShort("获取基础题型错误 ${it.message}")
                })
        } else if (type == "funny") {
            var empty = CommentRequestBean.getEmpty()
            empty.id = code!!
            var requestBean = CommentRequestBean(empty, CommentRequestBean.getHeader())
            model.api.getFunnyDetails(requestBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.success) {
                        Log.i("fengao_xiaomi", "loadData: " + Gson().toJson(it))
                        detail2 = it
                        brief.value = detail2!!.data.brief
                        this.name.value = detail2!!.data.name
                        imgUrl.value = detail2!!.data.faceImage
                    }
                }, {
                    it.printStackTrace()
                    Log.i("fengao_xiaomi", "loadData: ${it.message}")
                })
        }

    }

    var showSexChoose = ObservableBoolean(false)
    var sex = ObservableInt(0) // 0 男 1 女
    var showMarryChoose = ObservableBoolean(false)
    var marry = ObservableInt(0) // 0 未婚 1 已婚 2 离婚 3 丧偶
    var url =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1113%2F0F420110430%2F200F4110430-6-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1658728824&t=3c80f1fe4665ce36ff2063dc4b985308"
    var startTest = BindingCommand<String>(BindingAction {
        if (type == "normal") {
            jump("", detail, "", type, name.value)
        } else {
            jump("", detail2, "", type, name.value)
        }
    })

    var marryClickA = BindingCommand<String>(BindingAction {
        marry.set(0)
    })
    var marryClickB = BindingCommand<String>(BindingAction {
        marry.set(1)
    })
    var marryClickC = BindingCommand<String>(BindingAction {
        marry.set(2)
    })
    var marryClickD = BindingCommand<String>(BindingAction {
        marry.set(3)
    })
    var sexClickA = BindingCommand<String>(BindingAction {
        sex.set(0)
    })

    var sexClickB = BindingCommand<String>(BindingAction {
        sex.set(1)
    })
    var sexClick = BindingCommand<String>(BindingAction {
        var marry = when (marry.get()) {
            0 -> "未婚"
            1 -> "已婚"
            2 -> "离婚"
            3 -> "丧偶"
            else -> "未婚"
        }
        var sex = when (sex.get()) {
            0 -> "男"
            1 -> "女"
            else -> "男"
        }
        jump("", detail, "", type, name.value)
    })

    var marryClick = BindingCommand<String>(BindingAction {
        var marry = when (marry.get()) {
            0 -> "未婚"
            1 -> "已婚"
            2 -> "离婚"
            3 -> "丧偶"
            else -> "未婚"
        }
        jump("", detail, "", type, name.value)
    })

    @SuppressLint("CheckResult")
    fun jump(
        marry: String,
        bean: ScaDetailsResponseBean?,
        sex: String,
        type: String,
        name: String
    ) {
        if (jumpBasic) {
            var intent = Intent(getApplication(), TestBaseContentActivity::class.java)
            intent.putExtra("marry", marry)
            intent.putExtra("bean", bean)
            intent.putExtra("basebean", basicBean)
            intent.putExtra("sex", sex)
            intent.putExtra("type", type)
            intent.putExtra("name", name)
            intent.putExtra("scaRecId", scaCode)
            activity.finish()
            activity.startActivity(intent)
        } else {
            var intent = Intent(getApplication(), TestContentActivity::class.java)
            intent.putExtra("marry", marry)
            intent.putExtra("bean", bean)
            intent.putExtra("sex", sex)
            intent.putExtra("type", type)
            intent.putExtra("name", name)
            activity.finish()
            activity.startActivity(intent)
        }
    }

    fun jump(
        marry: String,
        bean: FunnyTestBean?,
        sex: String,
        type: String,
        name: String
    ) {
        var intent = Intent(getApplication(), TestContentActivity::class.java)
        intent.putExtra("marry", marry)
        intent.putExtra("bean", bean)
        intent.putExtra("sex", sex)
        intent.putExtra("type", type)
        intent.putExtra("name", name)
        activity.finish()
        activity.startActivity(intent)
    }
}