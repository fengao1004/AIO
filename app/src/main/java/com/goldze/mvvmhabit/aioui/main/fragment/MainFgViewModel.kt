package com.goldze.mvvmhabit.aioui.main.fragment

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
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
class MainFgViewModel(application: Application) : BaseViewModel<HttpRepository>(application) {

//    var gotoGonggao: BindingCommand<String> = BindingCommand(BindingAction {
//        startActivity(NoticeActivity::class.java)
//    })

    var bannerLiveData: MutableLiveData<List<BannerBeanData>> = MutableLiveData()

    var gotoScan: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(VideoActivity::class.java)
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

    var gotoZixun: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(ZixunActivity::class.java)
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
    fun loadData() {
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