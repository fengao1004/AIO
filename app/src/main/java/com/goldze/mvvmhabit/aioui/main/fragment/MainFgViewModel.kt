package com.goldze.mvvmhabit.aioui.main.fragment

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.RequestHeader
import com.goldze.mvvmhabit.aioui.clazz.ClazzActivity
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.kepu.KepuActivity
import com.goldze.mvvmhabit.aioui.knows.KnowsActivity
import com.goldze.mvvmhabit.aioui.notice.NoticeActivity
import com.goldze.mvvmhabit.aioui.relax.RelaxActivity
import com.goldze.mvvmhabit.aioui.test.TestActivity
import com.goldze.mvvmhabit.aioui.bean.TextObserver
import com.goldze.mvvmhabit.aioui.gonggao.GonggaoActivity
import com.goldze.mvvmhabit.aioui.main.bean.*
import com.goldze.mvvmhabit.aioui.video.VideoActivity
import com.goldze.mvvmhabit.aioui.zixun.ZixunActivity
import com.goldze.mvvmhabit.aioui.zixun.input.InputActivity
import com.goldze.mvvmhabit.aioui.zixun.phone.PhoneRVModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.utils.SPUtils
import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class MainFgViewModel(application: Application) : BaseViewModel<HttpRepository>(application) {

//    var gotoGonggao: BindingCommand<String> = BindingCommand(BindingAction {
//        startActivity(NoticeActivity::class.java)
//    })


    var showEditCode = ObservableBoolean(false)
    var bannerLiveData: MutableLiveData<List<BannerBeanData>> = MutableLiveData()
    var sbLiveData: MutableLiveData<String> = MutableLiveData()
    var gotoScan: BindingCommand<String> = BindingCommand(BindingAction {
//        startActivity(VideoActivity::class.java)
        ToastUtils.showShort("功能暂未实现")
    })

    var gotoTest: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(TestActivity::class.java)
    })

    var gotoClass: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(ClazzActivity::class.java)
    })

    var gotoKnows: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(KnowsActivity::class.java)
    })

    var gotoRelax: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(RelaxActivity::class.java)
    })

    var gotoKepu: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(KepuActivity::class.java)
    })

    @SuppressLint("CheckResult")
    var gotoZixun: BindingCommand<String> = BindingCommand(BindingAction {
        model.api.getPhoneList(
            CommentRequestBean(
                CommentRequestBean.DEFAULT,
                CommentRequestBean.getHeader()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success && !it.data.records.isNullOrEmpty()) {
                    startActivity(ZixunActivity::class.java)
                } else {
                    startActivity(InputActivity::class.java)
                }
            }, {
                startActivity(InputActivity::class.java)
            })

    })

    var gotoGonggao: BindingCommand<String> = BindingCommand(BindingAction {
        if (hasGonggao) {
            startActivity(GonggaoActivity::class.java)
        } else {
            ToastUtils.showShort("暂无公告")
        }
    })


    init {
        model = HttpRepository()
    }

    var gonggao = TextObserver("暂无公告")

    var hasGonggao = false

    @SuppressLint("CheckResult")
    fun activate(code: String) {
        var header = RequestHeader(code, Util.uniqueCode)
        var bean = CommentRequestBean(CommentRequestBean.DEFAULT, header)
        model.api.activation(bean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success && it.data.success) {
                    ToastUtils.showShort("激活成功")
                    showEditCode.set(false)
                    Util.serialNumber = code
                    SPUtils.getInstance().put("serialNumber", code)
                    Util.serialNumber = code
                    loadData()
                } else {
                    ToastUtils.showShort("激活失败，请确认激活码正确")
                }
            }, {
                ToastUtils.showShort("激活失败，请确认激活码正确")
            })
    }

    fun loadId(): Boolean {
        var serialNumber = SPUtils.getInstance().getString("serialNumber", "")
        var uniqueCode = SPUtils.getInstance().getString("uniqueCode", "")
        if (uniqueCode.isNullOrEmpty()) {
            uniqueCode =
                Settings.System.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
            SPUtils.getInstance().put("uniqueCode", uniqueCode)
            Util.uniqueCode = uniqueCode
        }
        return if (serialNumber.isNullOrEmpty()) {
            showEditCode.set(true)
            false
        } else {
            true
        }
    }

    @SuppressLint("CheckResult")
    fun loadData() {
//        if (!loadId()) {
//            return
//        }
        var bean = GetAnnounListRequestBean(
            GetAnnounListRequestBeanRequestBody(0, 0, 100, "", 1),
            GetAnnounListRequestBeanRequestHeader(
                Util.serialNumber, Util.uniqueCode
            )
        )
        model.api.getAnnounList(bean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success && it.data.records.isNotEmpty()) {
                    gonggao.value = it.data.records[0].name
                    hasGonggao = true
                } else {
                    gonggao.value = "暂无公告"
                }
            }, {
                gonggao.value = "暂无公告"
                it.printStackTrace()
            })
        var commonBean =
            CommentRequestBean(CommentRequestBean.DEFAULT, CommentRequestBean.getHeader())
        model.api.getEquipmentDetail(commonBean)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    Util.shebeiXq = it.data.data
                    sbLiveData.postValue("")
                }
            }, {

            })

        model.api.loadBanner(
            CommentRequestBean(
                CommentRequestBean.DEFAULT,
                CommentRequestBean.getHeader()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    var list = arrayListOf<BannerBeanData>()
                    it.data.forEach {
                        list.add(it)
                    }
                    bannerLiveData.postValue(list)
                } else {
                    gonggao.value = "暂无公告"
                }
            }, {
                gonggao.value = "暂无公告"
                it.printStackTrace()
            })
    }
}