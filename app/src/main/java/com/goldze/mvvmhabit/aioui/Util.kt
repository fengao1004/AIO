package com.goldze.mvvmhabit.aioui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.clazz.ClazzActivity
import com.goldze.mvvmhabit.aioui.clazz.content.ClazzContentActivity
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.knows.Knows2Activity
import com.goldze.mvvmhabit.aioui.knows.content.KnowsContentActivity
import com.goldze.mvvmhabit.aioui.main.bean.ShebeiXQBeanDataX
import com.goldze.mvvmhabit.aioui.relax.film.FilmFragment
import com.goldze.mvvmhabit.aioui.relax.gallery.GalleryFragment
import com.goldze.mvvmhabit.aioui.relax.meditation.MeditationFragment
import com.goldze.mvvmhabit.aioui.relax.music.MusicFragment
import com.goldze.mvvmhabit.aioui.test.TestActivity
import com.goldze.mvvmhabit.aioui.test.basecontent.TestBaseContentActivity
import com.goldze.mvvmhabit.aioui.test.bean.BasicDetailsResponseBeanData
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsRequestBean
import com.goldze.mvvmhabit.aioui.test.content.TestContentActivity
import com.goldze.mvvmhabit.aioui.video.VideoActivity
import com.goldze.mvvmhabit.aioui.video.bean.VideoBean
import com.goldze.mvvmhabit.aioui.webview.WebViewActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.ContainerActivity
import me.goldze.mvvmhabit.utils.ToastUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/25
 * Time: 11:59 下午
 */
object Util {
    var serialNumber = ""
    var uniqueCode = ""
    var shebeiXq: ShebeiXQBeanDataX? = null
    val model by lazy {
        HttpRepository()
    }

    /**
     * 测评 scale
    心理资讯 info
    放松音乐 music
    心理课堂 course
    冥想视频 meditation
    心理漫画 cartoon
    视频短片 film
    趣味测评 interest
     */
    @SuppressLint("CheckResult")
    fun jump(sysModuleCode: String, resourcesId: String, activity: Activity, resourcesName: String, code: String = "") {
        if (resourcesId.isNullOrEmpty()) {
            when (sysModuleCode) {
                "scale" -> {
                    activity.startActivity(Intent(activity, TestActivity::class.java))
                }
                "info" -> {
                    activity.startActivity(Intent(activity, Knows2Activity::class.java))
                }
                "music" -> {
                    val intent = Intent(activity, ContainerActivity::class.java)
                    intent.putExtra(
                        ContainerActivity.FRAGMENT,
                        MusicFragment::class.java.canonicalName
                    )
                    activity.startActivity(intent)
                }
                "course" -> {
                    activity.startActivity(Intent(activity, ClazzActivity::class.java))
                }
                "meditation" -> {
                    val intent = Intent(activity, ContainerActivity::class.java)
                    intent.putExtra(
                        ContainerActivity.FRAGMENT,
                        MeditationFragment::class.java.canonicalName
                    )
                    activity.startActivity(intent)
                }
                "cartoon" -> {
                    val intent = Intent(activity, ContainerActivity::class.java)
                    intent.putExtra(
                        ContainerActivity.FRAGMENT,
                        GalleryFragment::class.java.canonicalName
                    )
                    activity.startActivity(intent)
                }
                "film" -> {
                    val intent = Intent(activity, ContainerActivity::class.java)
                    intent.putExtra(
                        ContainerActivity.FRAGMENT,
                        FilmFragment::class.java.canonicalName
                    )
                    activity.startActivity(intent)
                }
                "interest" -> {
                    activity.startActivity(Intent(activity, TestActivity::class.java))
                }
            }
            return
        }
        when (sysModuleCode) {
            "scale" -> {
                var jumpBasic = false
                var onceId = ""
                var basicBean: BasicDetailsResponseBeanData? = null
                model.api.getScaBasics(code)
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
                        ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                if (it.success) {
                                    if (jumpBasic) {
                                        var intent =
                                            Intent(activity, TestBaseContentActivity::class.java)
                                        intent.putExtra("marry", "")
                                        intent.putExtra("bean", it)
                                        intent.putExtra("basebean", basicBean)
                                        intent.putExtra("sex", "")
                                        intent.putExtra("type", "normal")
                                        intent.putExtra("name", resourcesName)
                                        intent.putExtra("scaRecId", code)
                                        activity.finish()
                                        activity.startActivity(intent)
                                    } else {
                                        var intent =
                                            Intent(activity, TestContentActivity::class.java)
                                        intent.putExtra("marry", "")
                                        intent.putExtra("sex", "")
                                        intent.putExtra("type", "normal")
                                        intent.putExtra("bean", it)
                                        intent.putExtra("name", resourcesName)
                                        intent.putExtra("scaRecId", code)
                                        activity.startActivity(intent)
                                    }
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


            }
            "info" -> {
                var empty = CommentRequestBean.getEmpty()
                empty.id = resourcesId
                var header = CommentRequestBean.getHeader()
                model.api.getKnowsDetail(CommentRequestBean(empty, header))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.success) {
                            val intent =
                                Intent(activity, KnowsContentActivity::class.java)
                            intent.putExtra("bean", it.data)
                            activity.startActivity(intent)
                        } else {
                            ToastUtils.showShort("加载知识详情失败 ${it.message}")
                        }
                    }, {
                        ToastUtils.showShort("加载知识详情失败 ${it.message}")
                    })
            }
            "music" -> {
                val intent = Intent(activity, ContainerActivity::class.java)
                intent.putExtra(
                    ContainerActivity.FRAGMENT,
                    MusicFragment::class.java.canonicalName
                )
                var bundle = Bundle()
                bundle.putString("musicId", resourcesId)
                Log.i("fengao_xiaomi", "jump: id $resourcesId")
                intent.putExtra("bundle", bundle)
                activity.startActivity(intent)
            }
            "course" -> {
                val intent = Intent(activity, ClazzContentActivity::class.java)
                intent.putExtra("id", resourcesId.toLong())
                intent.putExtra("name", resourcesName)
                activity.startActivity(intent)
            }
            "meditation" -> {
                var empty = CommentRequestBean.getEmpty()
                empty.id = resourcesId
                var header = CommentRequestBean.getHeader()
                model.api.getMeditationDetail(CommentRequestBean(empty, header))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.success) {
                            val videoBean = VideoBean(
                                it.data.resourcesUrl ?: "",
                                it.data.name ?: "",
                                it.data.name ?: "",
                                it.data.meditationDescribe ?: "",
                                "点击数：${it.data.clickCount.toString()}"
                            )
                            val intent = Intent(activity, VideoActivity::class.java).apply {
                                putExtra("videoBean", videoBean)
                            }
                            activity.startActivity(intent)
                        } else {
                            ToastUtils.showShort("加载冥想视频详情失败 ${it.message}")
                        }
                    }, {
                        ToastUtils.showShort("加载冥想视频失败 ${it.message}")
                    })

            }
            "cartoon" -> {
                var empty = CommentRequestBean.getEmpty()
                empty.id = resourcesId
                var header = CommentRequestBean.getHeader()
                model.api.getCartoonDetail(CommentRequestBean(empty, header))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.success) {
                            WebViewActivity.start(
                                activity,
                                it.data.name ?: "",
                                it.data.content,
                                "1",
                                true,
                                WebViewActivity.MODE_SONIC
                            )
                        } else {
                            ToastUtils.showShort("加载漫画视频详情失败 ${it.message}")
                        }
                    }, {
                        ToastUtils.showShort("加载漫画视频失败 ${it.message}")
                    })
            }
            "film" -> {
                var empty = CommentRequestBean.getEmpty()
                empty.id = resourcesId
                var header = CommentRequestBean.getHeader()
                model.api.getFilmDetail(CommentRequestBean(empty, header))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.success) {
                            val videoBean = VideoBean(
                                it.data.resourcesUrl ?: "",
                                it.data.name ?: "",
                                it.data.name ?: "",
                                it.data.filmDescribe ?: "",
                                "点击数：${it.data.clickCount.toString()}"
                            )
                            val intent = Intent(activity, VideoActivity::class.java).apply {
                                putExtra("videoBean", videoBean)
                            }
                            activity.startActivity(intent)
                        } else {
                            ToastUtils.showShort("加载短片详情失败 ${it.message}")
                        }
                    }, {
                        ToastUtils.showShort("加载短片失败 ${it.message}")
                    })
            }
            "interest" -> {
                var empty = CommentRequestBean.getEmpty()
                empty.id = resourcesId
                var header = CommentRequestBean.getHeader()
                model.api.getFunnyDetails(CommentRequestBean(empty, header))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.success) {
                            var intent = Intent(activity, TestContentActivity::class.java)
                            intent.putExtra("marry", "")
                            intent.putExtra("sex", "")
                            intent.putExtra("type", "funny")
                            intent.putExtra("bean", it)
                            intent.putExtra("name", resourcesName)
                            activity.startActivity(intent)
                        } else {
                            ToastUtils.showShort("跳转失败 ${it.message}")
                        }
                    }, {
                        ToastUtils.showShort("跳转失败 ${it.message}")
                    })
            }
            else -> {
                ToastUtils.showShort("未知类型")
            }
        }
    }

    fun String.delHTMLTag(): String? {
        //定义script的正则表达式
        var htmlStr = this
        val regExScript = "<script[^>]*?>[\\s\\S]*?</script>"
        //定义style的正则表达式
        val regExStyle = "<style[^>]*?>[\\s\\S]*?</style>"
        //定义HTML标签的正则表达式
        val regExHtml = "<[^>]+>"
        val pScript: Pattern = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE)
        val mScript: Matcher = pScript.matcher(htmlStr)
        //过滤script标签
        htmlStr = mScript.replaceAll("")
        val pStyle: Pattern = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE)
        val mStyle: Matcher = pStyle.matcher(htmlStr)
        //过滤style标签
        htmlStr = mStyle.replaceAll("")
        val pHtml: Pattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE)
        val mHtml: Matcher = pHtml.matcher(htmlStr)
        //过滤html标签
        htmlStr = mHtml.replaceAll("")

        //返回文本字符串
        return htmlStr.trim { it <= ' ' }
    }

}