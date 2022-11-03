package com.aajeevika.clf.view.order.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aajeevika.clf.utility.app_enum.MyOrderType
import com.aajeevika.clf.view.sales.fragment.FragmentSales

class MyOrderPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = run {
        FragmentSales(
            when(position) {
                0 -> MyOrderType.COMPLETED
                else -> MyOrderType.PENDING
            }
        )
    }
}