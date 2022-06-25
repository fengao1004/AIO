package com.goldze.mvvmhabit.aioui.common.viewpagerfragment.listener

import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.goldze.mvvmhabit.R
import com.google.android.material.tabs.TabLayout

class AIOViewPagerOnTabSelectedListener(viewPager: ViewPager) : TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

    override fun onTabSelected(tab: TabLayout.Tab?) {
        super.onTabSelected(tab)
        val customView = tab?.customView
        if (customView is TextView) {
            customView.setTextColor(customView.context.resources.getColor(R.color.white))
            customView.setBackgroundResource(R.drawable.shape_stroke_label_select)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        super.onTabUnselected(tab)
        val customView = tab?.customView
        if (customView is TextView) {
            customView.setTextColor(customView.context.resources.getColor(R.color.tab_item_no_selected_text_color))
            customView.setBackgroundResource(R.drawable.shape_stroke_label_no_select)
        }
    }
}