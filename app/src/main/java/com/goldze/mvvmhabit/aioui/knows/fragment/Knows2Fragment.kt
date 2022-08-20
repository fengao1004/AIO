package com.goldze.mvvmhabit.aioui.knows.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.Util
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.knows.KnowsRecord
import com.goldze.mvvmhabit.aioui.knows.content.KnowsContentActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/21
 * Time: 10:01 下午
 */
class Knows2Fragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        //给ViewPager设置adapter
        binding.adapter =
            AIOViewPagerBindingAdapter(adapterClazz = Knows2RvBindingAdapter::class.java)
        binding.brRootView.visibility = View.GONE
        binding.adapter?.spanCount = 2
        viewModel.activity = activity
    }

    @SuppressLint("CheckResult")
    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            if (entity is KnowsRecord) {
                entity.clickCount = (entity.clickCount ?: 0) + 1
                entity.clickCountOb(entity.clickCount)
                val intent = Intent(viewModel.activity, KnowsContentActivity::class.java)
                intent.putExtra("bean", entity)
                viewModel.activity.startActivity(intent)
                var empty = CommentRequestBean.getEmpty()
                empty.id = entity.id.toString()
                var header = CommentRequestBean.getHeader()
                Util.model.api.getKnowsDetail(CommentRequestBean(empty, header))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                    }, {

                    })
            }
        }
    }

    override fun getTabType(): String {
        return "info"
    }

    override fun getRvItemLayoutResId(): Int {
        return R.layout.item_konws_rv
    }

    override fun getViewPagerRepository(): ListRepository {
        return KnowsARepository()
    }

    class KnowsARepository : ListRepository() {
        override fun getCommonListData(bean: CommentRequestBean): Observable<CommonListResponseBean<*>> {
            return api.getKnowsList(bean).map {
                it as CommonListResponseBean<*>
            }
        }
    }
}