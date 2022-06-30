package com.goldze.mvvmhabit.aioui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.clazz.ClazzActivity
import com.goldze.mvvmhabit.aioui.clazz.content.ClazzContentActivity
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.knows.KnowsActivity
import com.goldze.mvvmhabit.aioui.knows.content.KnowsContentActivity
import com.goldze.mvvmhabit.aioui.main.bean.ShebeiXQBeanDataX
import com.goldze.mvvmhabit.aioui.relax.RelaxActivity
import com.goldze.mvvmhabit.aioui.relax.film.FilmFragment
import com.goldze.mvvmhabit.aioui.relax.gallery.GalleryFragment
import com.goldze.mvvmhabit.aioui.relax.meditation.MeditationFragment
import com.goldze.mvvmhabit.aioui.relax.music.MusicFragment
import com.goldze.mvvmhabit.aioui.test.TestActivity
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsRequestBean
import com.goldze.mvvmhabit.aioui.test.content.TestContentActivity
import com.goldze.mvvmhabit.aioui.video.VideoActivity
import com.goldze.mvvmhabit.aioui.video.bean.VideoBean
import com.goldze.mvvmhabit.aioui.webview.WebViewActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.ContainerActivity
import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/25
 * Time: 11:59 下午
 */
object Util {
    var serialNumber = "123456"
    var uniqueCode = "b11fbef79a625aac"
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
    fun jump(code: String, id: String, activity: Activity, name: String) {
        if (id.isNullOrEmpty()) {
            when (code) {
                "scale" -> {
                    activity.startActivity(Intent(activity, TestActivity::class.java))
                }
                "info" -> {
                    activity.startActivity(Intent(activity, KnowsActivity::class.java))
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
        when (code) {
            "scale" -> {
                model.api.getScaDetails(ScaDetailsRequestBean(scaCode = id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.success) {
                            var intent = Intent(activity, TestContentActivity::class.java)
                            intent.putExtra("marry", "")
                            intent.putExtra("sex", "")
                            intent.putExtra("type", "normal")
                            intent.putExtra("bean", it)
                            intent.putExtra("name", name)
                            activity.startActivity(intent)
                        } else {
                            ToastUtils.showShort("跳转失败 ${it.message}")
                        }
                    }, {
                        ToastUtils.showShort("跳转失败 ${it.message}")
                    })
            }
            "info" -> {
                var empty = CommentRequestBean.getEmpty()
                empty.id = id
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
                activity.startActivity(Intent(activity, RelaxActivity::class.java))
            }
            "course" -> {
                val intent = Intent(activity, ClazzContentActivity::class.java)
                intent.putExtra("id", id.toInt())
                intent.putExtra("name", name)
                activity.startActivity(intent)
            }
            "meditation" -> {
                var empty = CommentRequestBean.getEmpty()
                empty.id = id
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
                empty.id = id
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
                empty.id = id
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
                empty.id = id
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
                            intent.putExtra("name", name)
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
}