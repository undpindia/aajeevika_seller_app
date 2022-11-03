package com.aajeevika.clf.view.sales

import android.content.Intent
import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivity
import com.aajeevika.clf.databinding.ActivitySalesBinding
import com.aajeevika.clf.view.sales.adapter.SalesPagerAdapter
import com.aajeevika.clf.view.sales.adapter.SalesRecyclerViewAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ActivitySales : BaseActivity<ActivitySalesBinding>(R.layout.activity_sales) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.run {
            viewPager.adapter = SalesPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.pending)
                    else -> getString(R.string.completed)
                }
            }.attach()
        }
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            addNewSaleBtn.setOnClickListener {
                val intent = Intent(this@ActivitySales, ActivityAddNewSale::class.java)
                startActivity(intent)
            }
        }
    }
}