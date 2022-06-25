package com.goldze.mvvmhabit.utils

import android.util.Log
import android.widget.Toast
import com.goldze.mvvmhabit.app.AppApplication
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSessionConfig
import top.xlxs.stuv.ui.common.ui.vassonic.SonicRuntimeImpl

/**
 * 弹出Toast提示。
 *
 * @param duration 显示消息的时间  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
 */
fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(AppApplication.getInstance(), this, duration).show()
}

/**
 * VasSonic预加载session。
 *
 * @param CharSequence 预加载url
 */
fun CharSequence.preCreateSession(): Boolean {
    if (!SonicEngine.isGetInstanceAllowed()) {
        SonicEngine.createInstance(
            SonicRuntimeImpl(AppApplication.getInstance()),
            SonicConfig.Builder().build()
        )
    }
    val sessionConfigBuilder = SonicSessionConfig.Builder().apply { setSupportLocalServer(true) }
    val preloadSuccess =
        SonicEngine.getInstance().preCreateSession(this.toString(), sessionConfigBuilder.build())
    Log.i(
        "preCreateSession()",
        "${this}\t:${if (preloadSuccess) "Preload start up success!" else "Preload start up fail!"}"
    )
    return preloadSuccess
}