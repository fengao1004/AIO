package com.goldze.mvvmhabit.aioui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.databinding.FragmentIntroduceBinding
import com.goldze.mvvmhabit.databinding.FragmentTestABinding
import me.goldze.mvvmhabit.base.BaseFragment

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/21
 * Time: 10:01 下午
 */
class TestAFragment : BaseFragment<FragmentTestABinding, TestAModel>() {
    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_test_a
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }


}