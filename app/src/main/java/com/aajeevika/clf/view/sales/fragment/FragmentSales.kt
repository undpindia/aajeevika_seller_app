package com.aajeevika.clf.view.sales.fragment

import android.os.Bundle
import android.view.View
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseFragmentVM
import com.aajeevika.clf.databinding.FragmentSalesBinding
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.utility.app_enum.MyOrderType
import com.aajeevika.clf.view.sales.adapter.SalesRecyclerViewAdapter
import com.aajeevika.clf.view.sales.viewmodel.SalesViewModel

class FragmentSales(private val myOrderType: MyOrderType) : BaseFragmentVM<FragmentSalesBinding, SalesViewModel>(
    R.layout.fragment_sales,
    SalesViewModel::class
) {
    private val mAdapter = SalesRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = mAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.getSalesList(myOrderType.type)
    }

    override fun observeData() {
        super.observeData()
        viewModel.salesLiveData.observe(viewLifecycleOwner,{
            stopSwipeToRefreshRefresh()
            mAdapter.addData(it.order_list)
        })
    }

    override fun initListener() {
        super.initListener()

        viewDataBinding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getSalesList(myOrderType.type)
        }
    }
}