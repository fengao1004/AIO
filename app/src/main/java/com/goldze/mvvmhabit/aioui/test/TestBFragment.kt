package com.goldze.mvvmhabit.aioui.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.list.CommonListResponseBean
import com.goldze.mvvmhabit.aioui.bean.list.TestRecord
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.AIOViewPagerFragment
import com.goldze.mvvmhabit.aioui.common.viewpagerfragment.adapter.AIOViewPagerBindingAdapter
import com.goldze.mvvmhabit.aioui.http.ListRepository
import com.goldze.mvvmhabit.aioui.main.fragment.IntroduceFgViewModel
import com.goldze.mvvmhabit.aioui.test.dec.TestDecActivity
import com.goldze.mvvmhabit.databinding.FragmentIntroduceBinding
import com.goldze.mvvmhabit.databinding.FragmentTestABinding
import com.goldze.mvvmhabit.databinding.FragmentTestBBinding
import io.reactivex.Observable
import me.goldze.mvvmhabit.base.BaseFragment

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/21
 * Time: 10:01 下午
 */
class TestBFragment : AIOViewPagerFragment() {
    override fun initData() {
        super.initData()
        //给ViewPager设置adapter
        binding.adapter =
            AIOViewPagerBindingAdapter(adapterClazz = TestBRvBindingAdapter::class.java)
        binding.brRootView.visibility = View.GONE
        binding.adapter?.spanCount = 1
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.itemClickEvent.observe(this) { entity ->
            if (entity is TestRecord) {
                entity.clickCount = (entity.clickCount?:0)+1
                entity.clickCountOb(entity.clickCount)
                var intent = Intent(activity, TestDecActivity::class.java)
                intent.putExtra("code", entity.id.toString())
                intent.putExtra("name", entity.name)
                intent.putExtra("type", "funny")
                startActivity(intent)
            }
        }
    }

    override fun getTabType(): String {
        Log.i("fengao_xiaomi", "getTabType: ")
        return "interest"
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
            return api.getFunTestList(bean).map {
                it as CommonListResponseBean<*>
            }
        }
    }


}