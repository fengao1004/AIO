package com.goldze.mvvmhabit.aioui.scan.qrcode

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.text.TextUtils
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.webview.WebViewFromUrlActivityA
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand


/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class QRCodeModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var mobileUrl = ""
    var deviceUrl = ""
    var reportId = 0L

    var start: BindingCommand<String> = BindingCommand(BindingAction {
        var intent = Intent(activity, WebViewFromUrlActivityA::class.java)
        intent.putExtra("title", "情绪分析")
        intent.putExtra("url", deviceUrl)
        intent.putExtra("id", reportId)
        activity.startActivity(intent)
        activity.finish()
    })

    var exit: BindingCommand<String> = BindingCommand(BindingAction {
        (activity as QRCodeActivity).gotoScan()
    })

    // 使用 ZXing 生成二维码
    fun getUrlQRCode(): Bitmap? {
        if (TextUtils.isEmpty(mobileUrl)) {
            return null
        }
        val bitMatrix = MultiFormatWriter().encode(mobileUrl, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val color: Int = if (bitMatrix[x, y])
                    activity.resources.getColor(R.color.black)
                else
                    activity.resources.getColor(R.color.white)
                bitmap.setPixel(x, y, color)
            }
        }
        return bitmap
    }
}