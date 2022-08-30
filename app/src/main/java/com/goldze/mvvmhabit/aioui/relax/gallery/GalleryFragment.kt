package com.goldze.mvvmhabit.aioui.relax.gallery

import android.annotation.SuppressLint
import android.widget.TextView
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.CartoonRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.viewmodel.AIOViewPagerItemViewModel
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.http.impl.GalleryRepository
import com.goldze.mvvmhabit.aioui.webview.WebViewActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.utils.ToastUtils

class GalleryFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        //给ViewPager设置adapter
        binding.adapter =
            AIOViewPagerBindingAdapter(adapterClazz = GalleryRvBindingAdapter::class.java)

        binding.brRootView.setPageTitle("心理图库")
        binding.adapter?.spanCount = 2
    }


    @SuppressLint("CheckResult")
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
                entity.clickCount = (entity.clickCount ?: 0) + 1
                entity.clickCountOb(entity.clickCount)
                var empty = CommentRequestBean.getEmpty()
                empty.id = entity.id.toString()
                var header = CommentRequestBean.getHeader()
                viewModel.model.api.getCartoonDetail(CommentRequestBean(empty, header))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                    }, {

                    })
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