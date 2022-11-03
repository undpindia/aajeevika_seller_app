package com.aajeevika.clf.view.order

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivity
import com.aajeevika.clf.databinding.ActivityMyOrderBinding
import com.aajeevika.clf.view.order.adapter.MyOrderPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ActivityMyOrder : BaseActivity<ActivityMyOrderBinding>(R.layout.activity_my_order) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.run {
            viewPager.adapter = MyOrderPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.completed)
                    else -> getString(R.string.pending)
                }
            }.attach()
        }
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            addSaleBtn.setOnClickListener {

            }
        }
    }
}