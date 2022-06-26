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
    var testTitle = TextObserver("")
    lateinit var marry: String
    lateinit var sex: String
    var index = ObservableInt(0)
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
    })

    var commitClick = BindingCommand<String>(BindingAction {
        commit()
    })

    private fun loadQue(index: Int) {
        if (index == 0) {
            lastIdStack.clear()
        }
        ques = detail.data.scaVo.quesList[index]
        if (ques == null) {
            return
        } else {
            startTime = System.currentTimeMillis()
            testTitle.setValue(ques!!.title)
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
        }
    }

    fun next(nextId: String) {
        if (nextId == "0") {
            commit()
        } else {
            lastIdStack.add(index.get())
            Log.i("fengao_xiaomi", "next: ${index.get()}")
            val index = map[nextId]
            loadQue(index ?: 0)
            this.index.set(index ?: 0)
        }
    }

    fun init() {
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
    }

    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun commit() {
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
        var data = AnserRequestData(AnserRequestDataParam(marry, sex), listA, detail.data.scaRecId)
        model.api.commit(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    var url = HttpRepository.BASE_H5_URL + "depressive?scaRecId=${it.data.scaRecId}"
                    var intent = Intent(activity, WebViewFromUrlActivity::class.java)
                    intent.putExtra("title", "报告")
                    intent.putExtra("url", url)
                    activity.startActivity(intent)
                    activity.finish()
                } else {
                    ToastUtils.showShort("提交错误，请稍后尝试")
                }
            }, {
                it.printStackTrace()
                ToastUtils.showShort("提交错误，请稍后尝试")
            })
    }

}
