package com.goldze.mvvmhabit.aioui.zixun.input

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.bean.TextObserver
import com.goldze.mvvmhabit.aioui.http.HttpRepository
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
class InputModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    init {
        model = HttpRepository()
    }

    var showZixunDialog = ObservableBoolean(false)
    var showQusDialog = ObservableBoolean(false)
    var zixunType = ObservableInt(-1)
    var wtlxType = ObservableInt(-1)
    var zixunClickA = BindingCommand<String>(BindingAction {
        zixunType.set(0)
    })
    var zixunClickB = BindingCommand<String>(BindingAction {
        zixunType.set(1)

    })
    var zixunClickC = BindingCommand<String>(BindingAction {
        zixunType.set(2)

    })
    var zixunClickD = BindingCommand<String>(BindingAction {
        zixunType.set(3)

    })
    var zixunClick = BindingCommand<String>(BindingAction {
        zxfs.value = when (zixunType.get()) {
            0 -> {
                "线下面对面咨询"
            }
            1 -> {
                "电话咨询"
            }
            2 -> {
                "视频咨询"
            }
            3 -> {
                "微信语音咨询"
            }
            else -> {
                "请选择"
            }
        }
        showZixunDialog.set(false)
    })

    var wtlxClickA = BindingCommand<String>(BindingAction {
        wtlxType.set(0)
    })
    var wtlxClickB = BindingCommand<String>(BindingAction {
        wtlxType.set(1)

    })
    var wtlxClickC = BindingCommand<String>(BindingAction {
        wtlxType.set(2)

    })
    var wtlxClickD = BindingCommand<String>(BindingAction {
        wtlxType.set(3)

    })
    var wtlxClickE = BindingCommand<String>(BindingAction {
        wtlxType.set(4)

    })
    var wtlxClickF = BindingCommand<String>(BindingAction {
        wtlxType.set(5)

    })
    var wtlxClickG = BindingCommand<String>(BindingAction {
        wtlxType.set(6)

    })
    var wtlxClickH = BindingCommand<String>(BindingAction {
        wtlxType.set(7)

    })
    var wtlxClickI = BindingCommand<String>(BindingAction {
        wtlxType.set(8)

    })
    var wtlxClick = BindingCommand<String>(BindingAction {
        wtlx.value = when (wtlxType.get()) {
            0 -> {
                "情绪压力"
            }
            1 -> {
                "婚姻情感"
            }
            2 -> {
                "家庭关系"
            }
            3 -> {
                "职场关系"
            }
            4 -> {
                "个人成长"
            }
            5 -> {
                "人际关系"
            }
            6 -> {
                "亲子关系"
            }
            7 -> {
                "性心理"
            }
            8 -> {
                "青少年"
            }
            else -> {
                "请选择"
            }
        }
        showQusDialog.set(false)
    })

    var cancelLogin = BindingCommand<String>(BindingAction {
        activity.finish()
    })

    @SuppressLint("CheckResult")
    fun commit(
        zxfsa: String,
        wtlxa: String,
        yyrq: String,
        yysj: String,
        jtwt: String,
        yydd: String
    ) {
        var bean = InputRequestBean(
            RequestBody(
                yydd,
                yyrq,
                yysj,
                zxfs.value,
                jtwt,
                0,
                username,
                password,
                0,
                100,
                "",
                wtlx.value,
                1,
                ""
            ), RequestHeader(Util.serialNumber, Util.uniqueCode)
        )
        model.api.commitInput(bean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    ToastUtils.showShort("预约成功")
                    activity.finish()
                } else {
                    ToastUtils.showShort("预约失败，请稍后再试 ${it.message}")
                }
            }, {
                ToastUtils.showShort("预约失败，请稍后再试 ${it.message}")
            })
    }

    lateinit var password: String
    lateinit var username: String
    var showLogin = ObservableBoolean(true)
    var zxfs: TextObserver = TextObserver("请选择")
    var wtlx: TextObserver = TextObserver("请选择")
    var jtwt: TextObserver = TextObserver("请选择")
    var yyrq: TextObserver = TextObserver("请选择")
    var yysj: TextObserver = TextObserver("请选择")
    var yydd: TextObserver = TextObserver("请选择")
}