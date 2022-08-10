package com.goldze.mvvmhabit.aioui.zixun.input

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.TimePicker
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityInputBinding
import me.goldze.mvvmhabit.base.BaseActivity
import me.goldze.mvvmhabit.utils.ToastUtils
import java.util.regex.Matcher
import java.util.regex.Pattern


class InputActivity : BaseActivity<ActivityInputBinding, InputModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_input
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    var typeFilter = InputFilter { source, start, end, dest, dstart, dend ->
        val p: Pattern = Pattern.compile("[^a-zA-Z0-9]")
        val m: Matcher = p.matcher(source.toString())
        if (!m.matches()) "" else null
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("在线预约")
//        binding.etCode.filters = arrayOf(typeFilter)
//        binding.etCodeA.filters = arrayOf(typeFilter)
        binding.tvCommit.setOnClickListener {
            var password = binding.etCode.text.toString()
            var username = binding.etCodeA.text.toString()
            if (password.isNullOrEmpty() || username.isNullOrEmpty()) {
                ToastUtils.showShort("请输入正确的账号和密码")
                return@setOnClickListener
            }
            viewModel.password = password
            viewModel.username = username
            viewModel.showLogin.set(false)
        }

        binding.tvZxfs.setOnClickListener {
            viewModel.showZixunDialog.set(true)
        }


        binding.tvWtlx.setOnClickListener {
            viewModel.showQusDialog.set(true)
        }
        binding.tvYyrq.setOnClickListener {
            val picker = DatePicker(this)
            var wheelLayout: DateWheelLayout = picker.wheelLayout;
            wheelLayout.setDateLabel("年", "月", "日");
            wheelLayout.setTextSize(45.0f)
            wheelLayout.yearLabelView.textSize = 38.0f
            wheelLayout.monthLabelView.textSize = 38.0f
            wheelLayout.dayLabelView.textSize = 38.0f
            wheelLayout.setSelectedTextSize(38.0f)
            wheelLayout.setResetWhenLinkage(false)
            picker.setOnDatePickedListener { a, b, c ->
                viewModel.yyrq.value = "$a-$b-$c"
            }
            picker.cancelView.text = "取消"
            picker.cancelView.textSize = 45.0f
            picker.okView.text = "确定"
            picker.okView.textSize = 45.0f
            picker.show()
        }
        binding.tvYysj.setOnClickListener {
            val picker = TimePicker(this)
            var wheelLayout = picker.wheelLayout;
            wheelLayout.setTimeMode(TimeMode.HOUR_24_NO_SECOND)
            wheelLayout.setTimeLabel("点", "分", "秒");
            wheelLayout.setTextSize(45.0f)
            wheelLayout.hourLabelView.textSize = 38.0f
            wheelLayout.minuteLabelView.textSize = 38.0f
            wheelLayout.secondLabelView.textSize = 38.0f
            wheelLayout.setSelectedTextSize(38.0f)
            picker.setOnTimeMeridiemPickedListener { hour, minute, second, isAnteMeridiem ->
                var ms = if (minute < 10) {
                    "0$minute"
                } else {
                    "$minute"
                }
                viewModel.yysj.value = "$hour:$ms"
            }
            picker.cancelView.text = "取消"
            picker.cancelView.textSize = 45.0f
            picker.okView.text = "确定"
            picker.okView.textSize = 45.0f
            picker.show()
        }

        binding.tvInputCommit.setOnClickListener {
            var zxfs = viewModel.zxfs.value
            var wtlx = viewModel.wtlx.value
            var yyrq = viewModel.yyrq.value
            var yysj = viewModel.yysj.value
            var jtwt = binding.etJtwt.text.toString()
            var yydd = binding.etYydd.text.toString()
            if ((zxfs.isNullOrEmpty() || zxfs == "请选择") ||
                (wtlx.isNullOrEmpty() || wtlx == "请选择") ||
                (yyrq.isNullOrEmpty() || yyrq == "请选择") ||
                (yysj.isNullOrEmpty() || yysj == "请选择")
            ) {
                ToastUtils.showShort("请补全必填内容再提交")
                return@setOnClickListener
            }
            viewModel.commit(zxfs, wtlx, yyrq, yysj, jtwt, yydd)
        }
    }
}
