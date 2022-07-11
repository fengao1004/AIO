package com.goldze.mvvmhabit.aioui.zixun.input

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.TimePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode
import com.github.gzuliyujiang.wheelpicker.contract.OnDatePickedListener
import com.github.gzuliyujiang.wheelpicker.contract.TimeFormatter
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.ActivityInputBinding
import me.goldze.mvvmhabit.base.BaseActivity
import me.goldze.mvvmhabit.utils.ToastUtils


class InputActivity : BaseActivity<ActivityInputBinding, InputModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_input
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("在线预约")
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
            MaterialDialog.Builder(this)
                .title("咨询方式")
                .positiveText("确认")
                .items(R.array.items)
                .itemsCallbackSingleChoice(
                    -1
                ) { dialog, itemView, which, text ->
                    viewModel.zxfs.value = text.toString()
                    true
                }
                .show();
        }
        binding.tvWtlx.setOnClickListener {
            MaterialDialog.Builder(this)
                .title("问题类型")
                .positiveText("确认")
                .items(R.array.qs_type)
                .itemsCallbackSingleChoice(
                    -1
                ) { dialog, itemView, which, text ->
                    viewModel.wtlx.value = text.toString()
                    true
                }
                .show();
        }
        binding.tvYyrq.setOnClickListener {
            val picker = DatePicker(this)
            var wheelLayout: DateWheelLayout = picker.wheelLayout;
            wheelLayout.setDateLabel("年", "月", "日");
            wheelLayout.setTextSize(40.0f)
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
            wheelLayout.setTextSize(40.0f)
            wheelLayout.setTimeMode(TimeMode.HOUR_24_NO_SECOND)
            wheelLayout.setTimeLabel("点", "分", "秒");
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
            if (zxfs.isNullOrEmpty() ||
                wtlx.isNullOrEmpty() ||
                yyrq.isNullOrEmpty() ||
                yysj.isNullOrEmpty()
            ) {
                ToastUtils.showShort("请补全必填内容再提交")
                return@setOnClickListener
            }
            viewModel.commit(zxfs, wtlx, yyrq, yysj, jtwt, yydd)
        }
    }
}