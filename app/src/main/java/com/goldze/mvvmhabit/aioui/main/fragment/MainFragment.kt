package com.goldze.mvvmhabit.aioui.main.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.brioal.adtextviewlib.view.OnAdChangeListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.gonggao.GonggaoActivity
import com.goldze.mvvmhabit.databinding.FragmentMainBinding
import com.stx.xhb.androidx.entity.BaseBannerInfo
import me.goldze.mvvmhabit.base.BaseFragment
import me.goldze.mvvmhabit.utils.ToastUtils


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : BaseFragment<FragmentMainBinding, MainFgViewModel>() {
    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()

        viewModel.activity = activity
        binding.ivFoucs.requestFocus()
        viewModel.bannerLiveData.observe(this) {
            var list = arrayListOf<BaseBannerInfo>()
            it.forEach {
                var info =
                    UrlBannerInfo(
                        it.faceUrl,
                        it.name,
                        it.sysModuleCode,
                        it.resourcesId,
                        it.code ?: ""
                    )
                list.add(info)
            }
            binding.xbanner.setBannerData(list) //setData（）方法已过时，推荐使用setBannerData（）方法，具体参照demo使用
        }
        //代码设置框架占位图，也可在布局中设置
        binding.xbanner.setBannerPlaceholderImg(R.drawable.moren, ImageView.ScaleType.CENTER_CROP)
        binding.xbanner.loadImage { banner, model, view, position ->
            Glide.with(view.context)
                .load((model as UrlBannerInfo).url)
                .apply(RequestOptions().placeholder(R.drawable.loading))
                .into((view as ImageView))
        }

        val texts: MutableList<String> = ArrayList()
        texts.add("暂无公告")
        binding.adTextview.setInterval(10000)
        binding.tvGonggao.visibility = View.VISIBLE
        binding.adTextview.visibility = View.GONE
        viewModel.gonggaoLiveData.observe(this) {
            binding.tvGonggao.visibility = View.GONE
            binding.adTextview.visibility = View.VISIBLE
            binding.adTextview.init(it) { textView, index ->
                textView.setTextColor(Color.parseColor("#3359e8"))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40.0f)
                textView.setOnClickListener {
                    startActivity(GonggaoActivity::class.java)
                }
            }
        }
        binding.xbanner.setOnItemClickListener { banner, model, view, position ->
            if ((model as UrlBannerInfo).sysModuleCode.isNullOrEmpty()) {
                ToastUtils.showShort("没有跳转目标")
                return@setOnItemClickListener
            } else {
                var info = model as UrlBannerInfo
                Util.jump(info.sysModuleCode, info.resId, activity!!, info.name, info.code)
            }
        }

        binding.tvEnter.setOnClickListener {
            var code = binding.etCode.text.toString()
            if (code.isNullOrEmpty()) {
                ToastUtils.showShort("请输入有效激活码")
            } else {
                viewModel.activate(code)
            }
        }

        viewModel.sbLiveData.observe(this) {
            Glide.with(MainFragment@ this)
                .load(Util.shebeiXq?.logo ?: "")
                .apply(RequestOptions().placeholder(R.drawable.loading))
                .into(binding.ivSmallLog)
            binding.tvTitle.text = Util.shebeiXq?.name ?: ""
            try {
                binding.rlRoot.setBackgroundColor(
                    Color.parseColor(
                        Util.shebeiXq?.backColour ?: "#ffffff"
                    )
                )
            } catch (e: Exception) {
                binding.rlRoot.setBackgroundColor(Color.parseColor("#ffffff"))
            }

        }
        if (viewModel.loadId()) {
            viewModel.loadData()
        }
    }
}

class UrlBannerInfo(
    var url: String,
    var name: String,
    var sysModuleCode: String,
    var resId: String,
    var code: String
) :
    BaseBannerInfo {
    override fun getXBannerUrl(): String {
        return url
    }

    override fun getXBannerTitle(): String {
        return name
    }

}