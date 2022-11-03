package com.aajeevika.clf.view.order

import BaseUrls
import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityOrderDetailsCompletedBinding
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.view.order.adapter.OrderDetailsProductRecyclerViewAdapter
import com.aajeevika.clf.view.order.viewmodel.OrderDetailsViewModel

class ActivityOrderDetailsCompleted : BaseActivityVM<ActivityOrderDetailsCompletedBinding, OrderDetailsViewModel>(
    R.layout.activity_order_details_completed,
    OrderDetailsViewModel::class
) {
    private val orderDetailsProductRecyclerViewAdapter = OrderDetailsProductRecyclerViewAdapter()
    private val orderId by lazy { intent.getIntExtra(Constant.ORDER_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.adapter = orderDetailsProductRecyclerViewAdapter

        viewModel.getCompletedOrderList(orderId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.orderDetailsLiveData.observe(this, {
            orderDetailsProductRecyclerViewAdapter.addData(it.items)

            viewDataBinding.run {
                orderId = it.order_id_d
                interestId = it.interest.interest_Id
                buyerName = it.buyer.name
                buyerMobile = it.buyer.mobile
                buyerEmail = it.buyer.email
                totalAmount = orderDetailsProductRecyclerViewAdapter.getTotalPrice().toString()
                UtilityActions.parseInterestDate(it.created_at)?.run { orderDate = UtilityActions.formatInterestDate(this) }

                sellerName = it.seller.organization_name
                sellerRating = it.seller_rating?.rating ?: 0F
                sellerReviewMessage = it.seller_rating?.review_msg
                sellerProfileImage = it.seller.profileImage?.run { BaseUrls.baseUrl + this }

                buyerName = it.buyer.name
                buyerRating = it.buyer_rating?.rating ?: 0F
                buyerReviewMessage = it.buyer_rating?.review_msg
                buyerProfileImage = it.buyer.profileImage?.run { BaseUrls.baseUrl + this }
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }
}