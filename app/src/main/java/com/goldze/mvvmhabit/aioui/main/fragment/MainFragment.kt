package com.goldze.mvvmhabit.aioui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.FragmentMainBinding
import com.stx.xhb.androidx.entity.BaseBannerInfo
import me.goldze.mvvmhabit.base.BaseFragment


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
        binding.ivFoucs.requestFocus()
        viewModel.loadData()
        viewModel.bannerLiveData.observe(this) {
            var list = arrayListOf<BaseBannerInfo>()
            it.forEach {
                var info = UrlBannerInfo(it.faceUrl, it.name)
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
    }
}

class UrlBannerInfo(var url: String, var name: String) : BaseBannerInfo {
    override fun getXBannerUrl(): String {
        return url
    }

    override fun getXBannerTitle(): String {
        return name
    }

}