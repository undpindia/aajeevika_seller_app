package com.aajeevika.clf.view.order

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityOrderDetailsCompletedBinding
import com.aajeevika.clf.databinding.ActivityOrderDetailsPendingBinding
import com.aajeevika.clf.view.order.adapter.OrderDetailsProductRecyclerViewAdapter
import com.aajeevika.clf.view.order.viewmodel.OrderDetailsViewModel

class ActivityOrderDetailsPending : BaseActivityVM<ActivityOrderDetailsPendingBinding, OrderDetailsViewModel>(
    R.layout.activity_order_details_pending,
    OrderDetailsViewModel::class
) {
    private val orderDetailsProductRecyclerViewAdapter = OrderDetailsProductRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.adapter = orderDetailsProductRecyclerViewAdapter
    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }
}