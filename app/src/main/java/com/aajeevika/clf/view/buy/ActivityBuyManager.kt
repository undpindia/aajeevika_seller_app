package com.aajeevika.clf.view.buy

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityBuyManagerBinding
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.view.buy.adapter.BuyManagerRecyclerViewAdapter
import com.aajeevika.clf.view.buy.viewmodel.BuyManagerViewModel

class ActivityBuyManager : BaseActivityVM<ActivityBuyManagerBinding, BuyManagerViewModel>(
    R.layout.activity_buy_manager,
    BuyManagerViewModel::class
) {
    private val buyManagerRecyclerViewAdapter = BuyManagerRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getIndividualSalesList()

        viewDataBinding.recyclerView.run {
            adapter = buyManagerRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.salesListLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            buyManagerRecyclerViewAdapter.addData(it.order_list)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getIndividualSalesList()
            }
        }
    }
}