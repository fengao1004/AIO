package com.goldze.mvvmhabit.aioui.main.fragment

import android.app.Application
import com.goldze.mvvmhabit.aioui.clazz.ClazzActivity
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.kepu.KepuActivity
import com.goldze.mvvmhabit.aioui.knows.KnowsActivity
import com.goldze.mvvmhabit.aioui.notice.NoticeActivity
import com.goldze.mvvmhabit.aioui.relax.RelaxActivity
import com.goldze.mvvmhabit.aioui.scan.ScanActivity
import com.goldze.mvvmhabit.aioui.test.TestActivity
import com.goldze.mvvmhabit.aioui.test.dec.TestDecActivity
import com.goldze.mvvmhabit.aioui.zixun.ZixunActivity
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class MainFgViewModel(application: Application) : BaseViewModel<HttpRepository>(application) {

    var gotoGonggao: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(NoticeActivity::class.java)
    })

    var gotoScan: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(ScanActivity::class.java)
    })

    var gotoTest: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(TestDecActivity::class.java)
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
}