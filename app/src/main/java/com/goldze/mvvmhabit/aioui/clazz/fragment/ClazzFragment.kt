package com.goldze.mvvmhabit.aioui.clazz.fragment

import android.content.Intent
import android.view.View
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.bean.list.TestRecord
import com.goldze.mvvmhabit.aioui.clazz.bean.ClazzListResponseBeanRecord
import com.goldze.mvvmhabit.aioui.clazz.content.ClazzContentActivity
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.http.ListRepository
import io.reactivex.Observable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/21
 * Time: 10:01 下午
 */
class ClazzFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        //给ViewPager设置adapter
        binding.adapter =
            AIOViewPagerBindingAdapter(adapterClazz = ClazzRvBindingAdapter::class.java)
        binding.brRootView.visibility = View.GONE
        binding.adapter?.spanCount = 2
        viewModel.activity = activity
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            if (entity is ClazzListResponseBeanRecord) {
                entity.clickCount = (entity.clickCount?:0)+1
                entity.clickCountOb(entity.clickCount)
                val intent = Intent(viewModel.activity, ClazzContentActivity::class.java)
                intent.putExtra("id", entity.id)
                intent.putExtra("name", entity.name)
                viewModel.activity.startActivity(intent)
            }
        }
    }

    override fun getTabType(): String {
        return "course"
    }

    override fun getRvItemLayoutResId(): Int {
        return R.layout.item_clazz_rv
    }

    override fun getViewPagerRepository(): ListRepository {
        return ClazzRepository()
    }

    class ClazzRepository : ListRepository() {
        override fun getCommonListData(bean: CommentRequestBean): Observable<CommonListResponseBean<*>> {
            return api.getClazzList(bean).map {
                it as CommonListResponseBean<*>
            }
        }
    }
}