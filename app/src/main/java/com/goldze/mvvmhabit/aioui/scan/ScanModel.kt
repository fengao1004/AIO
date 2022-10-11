package com.goldze.mvvmhabit.aioui.scan

import android.app.Application
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.scan.qingxu.QingxuActivity
import com.goldze.mvvmhabit.aioui.scan.yinsi.YinsiActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
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
class ScanModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var agree: Boolean = true
    var start: BindingCommand<String> = BindingCommand(BindingAction {
        if (!agree) {
            ToastUtils.showShort("请先同意隐私协议")
        } else {
            XXPermissions.with(activity)
                .permission(Permission.CAMERA)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                        if (!all) {
                            ToastUtils.showShort(("获取部分权限成功，但部分权限未正常授予"))
                            return
                        }
                        startActivity(QingxuActivity::class.java)
                        activity.finish()
                    }

                    override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                        if (never) {
                            ToastUtils.showShort("被永久拒绝授权，请手动授予读写文件和相机权限")
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(activity, permissions)
                        } else {
                            ToastUtils.showShort(("读写文件和相机权限失败，该功能暂时不可用"))
                        }
                    }
                })
        }
    })

    var check: BindingCommand<String> = BindingCommand(BindingAction {
        agree = !agree
        (activity as ScanActivity).setCheck(agree)
    })

    var yinsi: BindingCommand<String> = BindingCommand(BindingAction {
        startActivity(YinsiActivity::class.java)
    })
}