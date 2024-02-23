package com.goldze.mvvmhabit.aioui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.databinding.FragmentIntroduceBinding
import com.goldze.mvvmhabit.databinding.FragmentMainBinding
import me.goldze.mvvmhabit.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [IntroduceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IntroduceFragment : BaseFragment<FragmentIntroduceBinding, IntroduceFgViewModel>() {
    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_introduce
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.baseRoot.setPageTitle("项目介绍")
        binding.baseRoot.setAppTitle(Util.shebeiXq?.name ?: "")
        binding.baseRoot.setLogo(Util.shebeiXq?.logo ?: "")
        binding.webView.setInitialScale(220)
        binding.webView.loadDataWithBaseURL(
            null,
            Util.shebeiXq?.deptIntroduce,
            "text/html",
            "utf-8",
            null
        )
    }
}