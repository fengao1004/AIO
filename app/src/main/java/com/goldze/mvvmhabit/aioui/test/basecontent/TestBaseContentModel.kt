package com.goldze.mvvmhabit.aioui.test.basecontent

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
import com.goldze.mvvmhabit.aioui.test.content.TestContentActivity
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
class TestBaseContentModel(application: Application) : BaseViewModel<HttpRepository>(application) {
    var observableList = ObservableArrayList<TestBaseContentItemModel>()
    lateinit var detail: ScaDetailsResponseBean
    lateinit var basicBean: BasicDetailsResponseBeanData
    lateinit var type: String
    var testTitle = TextObserver("")
    lateinit var marry: String
    lateinit var sex: String
    var index = ObservableInt(0)
    var name = ""
    var scaRecId = ""
    var isNormal = ObservableBoolean(true)
    var progress = ObservableInt(0)
    var endIndex: String = ""
    var hasLast = ObservableBoolean(false)
    var itemBinding =
        ItemBinding.of<TestBaseContentItemModel>(BR.viewModel, R.layout.item_test_base)
    var ques: BasicDetailsResponseBeanQues? = null
    var showNext = ObservableBoolean(false)
    var showCommit = ObservableBoolean(false)
    var startTime = 0L
    var lastClick = BindingCommand<String>(BindingAction {
        if (index.get() > 0) {
            index.set(index.get() - 1)
            loadQue(index.get())
            hasLast.set(index.get() > 0)
        }
    })

    var nextClick = BindingCommand<String>(BindingAction {
        var ind = index.get()
        if (ind == basicBean.quesList.size - 1) {
            commit()
        } else {
            var count = 0
            basicBean?.quesList[index.get()]?.options.forEach {
                if (it.isCheck.get()) {
                    count++
                }
            }
            if (count == 0) {
                ToastUtils.showShort("请至少选择一个答案")
                return@BindingAction
            }
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
        ques = basicBean.quesList[index]
        if (ques == null) {
            return
        } else {
            startTime = System.currentTimeMillis()
            testTitle.setValue(ques!!.title)
            if (ques!!.type == 3 && index != basicBean.quesList.size - 1) {
                showNext.set(true)
            } else {
                showNext.set(false)
            }
            if (ques!!.type == 3 && index == basicBean.quesList.size - 1) {
                showCommit.set(true)
            } else {
                showCommit.set(false)
            }
            if (index + 1 == basicBean.quesList.size) {
                progress.set(100)
            } else {
                progress.set(((index + 1).toFloat() * 100 / basicBean.quesList.size.toFloat()).toInt())
            }
            observableList.clear()
            ques!!.options.forEach {
                observableList.add(TestBaseContentItemModel(it) { opt ->
                    if (ques!!.type == 2) {
                        ques!!.options.forEach { ita ->
                            if (opt == ita) {
                                opt.isCheck.set(true)
                            } else {
                                ita.isCheck.set(false)
                            }
                        }
                        if (index == basicBean.quesList.size - 1) {
                            showCommit.set(true)
                            showNext.set(false)
                        } else {
                            ques?.times = (System.currentTimeMillis() - startTime).toInt()
                            next()
                        }
                    } else {
                        opt.isCheck.set(!opt.isCheck.get())
                    }
                })
            }
        }
    }

    fun next() {
        loadQue(this.index.get() + 1)
        this.index.set(index.get() + 1)
    }


    fun init() {
        basicBean.quesList.forEach {
            it.options.forEach {
                it.isCheck = ObservableBoolean(false)
            }
        }
        observableList.clear()
        endIndex = "/${basicBean.quesList.size}"
        index.set(0)
        hasLast.set(false)
        loadQue(0)
    }

    init {
        model = HttpRepository()
    }

    @SuppressLint("CheckResult")
    fun commit() {
        ques?.times = (System.currentTimeMillis() - startTime).toInt()
        var listA = arrayListOf<BasicAnserBean>()
        basicBean.quesList.forEach {
            var result = ""
            var optIdList = arrayListOf<Int>()
            it.options.forEach {
                if (it.isCheck.get()) {
                    result += "${it.title},"
                    optIdList.add(it.id)
                }
            }
            result = result.substring(0, result.length - 1)
            var onceId = basicBean.onceId
            var basicsQuesId = it.id
            var quesData = BasicAnserBean(basicsQuesId, onceId, result, optIdList)
            listA.add(quesData)
        }

        model.api.commitBasic(listA)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success) {
                    var intent = Intent(getApplication(), TestContentActivity::class.java)
                    intent.putExtra("marry", marry)
                    intent.putExtra("bean", detail)
                    intent.putExtra("sex", sex)
                    intent.putExtra("type", type)
                    intent.putExtra("name", name)
                    activity.finish()
                    activity.startActivity(intent)
                } else {
                    ToastUtils.showShort("提交错误，请稍后尝试")
                }
            }, {
                it.printStackTrace()
                ToastUtils.showShort("提交错误，请稍后尝试")
            })
    }

}
