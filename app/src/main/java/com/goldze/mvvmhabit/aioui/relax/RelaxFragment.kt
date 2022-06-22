package com.goldze.mvvmhabit.aioui.relax

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.relax.music.MusicFragment
import com.goldze.mvvmhabit.databinding.FragmentRelaxBinding
import me.goldze.mvvmhabit.base.BaseFragment

class RelaxFragment : BaseFragment<FragmentRelaxBinding, RelaxFragmentModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_relax
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()

        binding.musicRelax.setOnClickListener {
            startContainerActivity(MusicFragment::class.java.canonicalName)
        }

        binding.mindRelax.setOnClickListener {

        }

        binding.mindGallery.setOnClickListener {

        }
    }

}