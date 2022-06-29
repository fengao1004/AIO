package com.goldze.mvvmhabit.aioui.test

import android.content.Intent
import android.util.Log
import android.view.View
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.bean.list.TestRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.test.dec.TestDecActivity
import io.reactivex.Observable

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/21
 * Time: 10:01 下午
 */
class TestAFragment : AIOViewPagerFragment() {

    override fun initData() {
        super.initData()
        //给ViewPager设置adapter
        binding.adapter =
            AIOViewPagerBindingAdapter(adapterClazz = TestARvBindingAdapter::class.java)
        binding.brRootView.visibility = View.GONE
        binding.adapter?.spanCount = 1
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            if (entity is TestRecord) {
                var intent = Intent(activity, TestDecActivity::class.java)
                intent.putExtra("code", entity.code)
                intent.putExtra("name", entity.name)
                startActivity(intent)
            }
        }
    }

    override fun getTabType(): String {
        Log.i("fengao_xiaomi", "getTabType: ")
        return "scale"
    }

    override fun getRvItemLayoutResId(): Int {
        Log.i("fengao_xiaomi", "getRvItemLayoutResId: ")
        return R.layout.item_rv_test
    }

    override fun getViewPagerRepository(): ListRepository {
        Log.i("fengao_xiaomi", "getViewPagerRepository: ")
        return TestRepository()
    }

    class TestRepository : ListRepository() {
        override fun getCommonListData(bean: CommentRequestBean): Observable<CommonListResponseBean<*>> {
            return api.getTestList(bean).map {
                it as CommonListResponseBean<*>
            }
        }
    }
}