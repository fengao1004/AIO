package com.goldze.mvvmhabit.aioui.relax.gallery

import android.widget.TextView
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.list.CartoonRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.http.impl.GalleryRepository
import com.goldze.mvvmhabit.aioui.webview.WebViewActivity

class GalleryFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        //给ViewPager设置adapter
        binding.adapter = AIOViewPagerBindingAdapter(adapterClazz = GalleryRvBindingAdapter::class.java)

        binding.brRootView.setPageTitle("心理图库")
        binding.adapter?.spanCount = 2
    }


    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            if (entity is CartoonRecord) {
                WebViewActivity.start(
                    viewModel.getApplication(),
                    entity.name ?: "",
                    entity.content,
                    "1",
                    true,
                    WebViewActivity.MODE_SONIC
                )
            }
        }
    }

    override fun getTabType(): String {
        return "cartoon"
    }

    override fun getRvItemLayoutResId(): Int {
        return R.layout.item_rv_gallery
    }

    override fun getViewPagerRepository(): ListRepository {
        return GalleryRepository()
    }
}