package com.goldze.mvvmhabit.aioui.knows.fragment

import android.content.Intent
import android.view.View
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.knows.KnowsRecord
import com.goldze.mvvmhabit.aioui.knows.content.KnowsContentActivity
import io.reactivex.Observable

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

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            if (entity is KnowsRecord) {
                val intent = Intent(viewModel.activity, KnowsContentActivity::class.java)
                intent.putExtra("bean", entity)
                viewModel.activity.startActivity(intent)
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