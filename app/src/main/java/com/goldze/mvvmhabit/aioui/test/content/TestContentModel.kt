package com.goldze.mvvmhabit.aioui.test.content

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.TextObserver
import com.goldze.mvvmhabit.aioui.http.HttpRepository
import com.goldze.mvvmhabit.aioui.test.bean.*
import com.goldze.mvvmhabit.aioui.test.result.FunnyResultActivity
import com.goldze.mvvmhabit.aioui.webview.WebViewFromUrlActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.utils.ToastUtils
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.util.*

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:56 下午
 */
class TestContentModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var observableList = ObservableArrayList<TestContentItemModel>()
    lateinit var detail: ScaDetailsResponseBean
    lateinit var detail2: FunnyTestBean
    lateinit var type: String
    var testTitle = TextObserver("")
    lateinit var marry: String
    lateinit var sex: String
    var index = ObservableInt(0)
    var name = ""
    var isNormal = ObservableBoolean(true)
    var progress = ObservableInt(0)
    var endIndex: String = ""
    var hasLast = ObservableBoolean(false)
    var itemBinding = ItemBinding.of<TestContentItemModel>(BR.viewModel, R.layout.item_test)
    var lastIdStack = Stack<Int>()
    var ques: Ques? = null
    var map: MutableMap<String, Int> = hashMapOf()
    var showNext = ObservableBoolean(false)
    var showCommit = ObservableBoolean(false)
    var startTime = 0L
    var lastClick = BindingCommand<String>(BindingAction {
        if (!lastIdStack.empty()) {
            var id = lastIdStack.pop()
            index.set(id)
            loadQue(index.get())
            hasLast.set(!lastIdStack.empty())
        }
    })

    var nextClick = BindingCommand<String>(BindingAction {
        var ind = index.get()
        if (type == "normal") {
            if (ind == detail.data.scaVo.quesList.size - 1) {
                commit()
            } else {
                var count = 0
                ques?.optList?.forEach {
                    if (it.isCheck.get()) {
                        count++
                    }
                }
                if (count == 0) {
                    ToastUtils.showShort("请至少选择一个答案")
                    return@BindingAction
                }
                lastIdStack.add(ind)
                ind++
                index.set(ind)
                ques?.times = (System.currentTimeMillis() - startTime).toInt()
                loadQue(index.get())
            }
        } else {
            if (ind == detail2.data.quesList.size - 1) {
                commit()
            } else {
                var count = 0
                ques?.optList?.forEach {
                    if (it.isCheck.get()) {
                        count++
                    }
                }
                if (count == 0) {
                    ToastUtils.showShort("请至少选择一个答案")
                    return@BindingAction
                }
                lastIdStack.add(ind)
                ind++
                index.set(ind)
                ques?.times = (System.currentTimeMillis() - startTime).toInt()
                loadQue(index.get())
            }
        }

    })

    var commitClick = BindingCommand<String>(BindingAction {
        commit()
    })

    private fun loadQue(index: Int) {
        if (index == 0) {
            lastIdStack.clear()
        }
        if (type == "normal") {
            ques = detail.data.scaVo.quesList[index]
        } else {
            ques = detail2.data.quesList[index]
        }
        if (ques == null) {
            return
        } else {
            startTime = System.currentTimeMillis()
            testTitle.setValue(ques!!.title)
            if (type == "normal") {
                if (ques!!.type == 2 && index != detail.data.scaVo.quesList.size - 1) {
                    showNext.set(true)
                } else {
                    showNext.set(false)
                }
                if (ques!!.type == 2 && index == detail.data.scaVo.quesList.size - 1) {
                    showCommit.set(true)
                } else {
                    showCommit.set(false)
                }
                if (index + 1 == detail.data.scaVo.quesList.size) {
                    progress.set(100)
                } else {
                    progress.set(((index + 1).toFloat() * 100 / detail.data.scaVo.quesList.size.toFloat()).toInt())
                }
                observableList.clear()
                ques!!.optList.forEach {
                    observableList.add(TestContentItemModel(it) { opt ->
                        if (ques!!.type == 1) {
                            ques!!.optList.forEach { ita ->
                                if (opt == ita) {
                                    opt.isCheck.set(true)
                                } else {
                                    ita.isCheck.set(false)
                                }
                            }
                            if (opt.nextQuesId == "0") {
                                showCommit.set(true)
                                showNext.set(false)
                            } else {
                                ques?.times = (System.currentTimeMillis() - startTime).toInt()
                                next(opt.nextQuesId)
                            }
                        } else {
                            opt.isCheck.set(!opt.isCheck.get())
                        }
                    })
                }
            } else {
                observableList.clear()
                ques!!.optList.forEach {
                    observableList.add(TestContentItemModel(it) { opt ->
                        ques!!.optList.forEach { ita ->
                            if (opt == ita) {
                                opt.isCheck.set(true)
                            } else {
                                ita.isCheck.set(false)
                            }
                        }
                        if (opt.nextQuesId == "0") {
                            showCommit.set(true)
                            showNext.set(false)
                        } else {
                            ques?.times = (System.currentTimeMillis() - startTime).toInt()
                        }
                    })
                }
            }

        }
    }

    fun next(nextId: String) {
        if (type == "normal") {
            if (nextId == "0") {
                commit()
            } else {
                lastIdStack.add(index.get())
                Log.i("fengao_xiaomi", "next: ${index.get()}")
                val index = map[nextId]
                loadQue(index ?: 0)
                this.index.set(index ?: 0)
            }
        } else {
            commit()
        }

    }


    fun init() {
        if (type == "normal") {
            detail.data.scaVo.quesList.forEach {
                map[it.id] = it.sort - 1
                it.optList.forEach {
                    it.isCheck = ObservableBoolean(false)
                }
            }
            observableList.clear()
            endIndex = "/${detail.data.scaVo.quesList.size}"
            lastIdStack.clear()
            index.set(0)
            loadQue(0)
        } else {
            isNormal.set(false)
            detail2.data.quesList.forEach {
                map[it.id] = it.sort - 1
                it.optList.forEach {
                    it.isCheck = ObservableBoolean(false)
                }
            }
            observableList.clear()
            endIndex = "/${detail2.data.quesList.size}"
            lastIdStack.clear()
            index.set(0)
            showCommit.set(true)
            showNext.set(false)
            loadQue(0)
        }

    }

    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun commit() {
        if (type == "normal") {
            ques?.times = (System.currentTimeMillis() - startTime).toInt()
            var listA = arrayListOf<AnserRequestDataQues>()
            detail.data.scaVo.quesList.forEach {
                var list = arrayListOf<String>()
                it.optList.forEach {
                    if (it.isCheck.get()) {
                        list.add(it.id)
                    }
                }
                var scaId = detail.data.scaRecId
                var ext1 = ""
                var quesId = it!!.id.toInt()
                var times = it!!.times
                var quesData = AnserRequestDataQues(ext1, list, quesId, scaId, times)
                listA.add(quesData)
            }
            var data =
                AnserRequestData(AnserRequestDataParam(marry, sex), listA, detail.data.scaRecId)
            model.api.commit(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.success) {
                        var pinjieUrl = when {
                            name.contains("焦虑") -> {
                                "/anxiety?scaRecId="
                            }
                            name.contains("抑郁") -> {
                                "/depressive?scaRecId="
                            }
                            name.contains("睡眠状况") -> {
                                "/sleep?scaRecId="
                            }
                            name.contains("艾森克人格") -> {
                                "/aiSenKe?scaRecId="
                            }
                            name.contains("大五人格") -> {
                                "/fiveRG?scaRecId="
                            }
                            name.contains("社会支持") -> {
                                "/comprehend?scaRecId="
                            }
                            name.contains("心理复原力") -> {
                                "/resilience?scaRecId="
                            }
                            name.contains("工作倦怠量") -> {
                                "/workJD?scaRecId="
                            }
                            name.contains("心理健康") -> {
                                "/psychological?scaRecId="
                            }
                            name.contains("应对方式") -> {
                                "/yingduifs?scaRecId="
                            }
                            name.contains("情绪稳定性") -> {
                                "/qingxuwd?scaRecId="
                            }
                            name.contains("工作投入") -> {
                                "/workTR?scaRecId="
                            }
                            name.contains("中小学心理健康") -> {
                                "/zxxxl?scaRecId="
                            }
                            name.contains("学业成就") -> {
                                "/xycj?scaRecId="
                            }
                            name.contains("中小学心理适应") -> {
                                "/zxxsy?scaRecId="
                            }
                            name.contains("家庭沟通") -> {
                                "/familyGT?scaRecId="
                            }
                            name.contains("同学关系") -> {
                                "/tongxuegx?scaRecId="
                            }
                            else -> {
                                "/anxiety?scaRecId="
                            }
                        }
                        var url = HttpRepository.BASE_H5_URL + pinjieUrl + it.data.scaRecId
                        var intent = Intent(activity, WebViewFromUrlActivity::class.java)
                        intent.putExtra("title", "报告")
                        intent.putExtra("url", url)
                        Log.i("fengao_xiaomi", "commit: $url")
                        activity.startActivity(intent)
                        activity.finish()
                    } else {
                        ToastUtils.showShort("提交错误，请稍后尝试")
                    }
                }, {
                    it.printStackTrace()
                    ToastUtils.showShort("提交错误，请稍后尝试")
                })
        } else {
            var result = ""
            detail2.data.quesList.forEach {
                var num = 0
                it.optList.forEach {
                    if (it.isCheck.get()) {
                        result = it.result
                    } else {
                        num++
                    }
                }
                if (num == it.optList.size) {
                    ToastUtils.showShort("请完成选择后再提交")
                    return
                }
            }
            var intent = Intent(activity, FunnyResultActivity::class.java)
            intent.putExtra("name", detail2.data.name)
            intent.putExtra("result", result)
            activity.startActivity(intent)
        }
    }

}
