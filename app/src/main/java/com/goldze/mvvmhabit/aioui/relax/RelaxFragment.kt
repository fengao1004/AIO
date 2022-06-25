package com.goldze.mvvmhabit.aioui.relax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.relax.gallery.GalleryFragment
import com.goldze.mvvmhabit.aioui.relax.music.MusicFragment
import com.goldze.mvvmhabit.aioui.relax.train.TrainFragment
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
            startContainerActivity(TrainFragment::class.java.canonicalName)
        }

        binding.mindGallery.setOnClickListener {
            startContainerActivity(GalleryFragment::class.java.canonicalName)
        }
    }

}