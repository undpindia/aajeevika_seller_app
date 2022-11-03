package com.aajeevika.clf.view.order.fragment

import android.os.Bundle
import android.view.View
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseFragmentVM
import com.aajeevika.clf.databinding.FragmentMyOrderBinding
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.utility.app_enum.MyOrderType
import com.aajeevika.clf.view.order.adapter.MyOrderRecyclerViewAdapter
import com.aajeevika.clf.view.order.viewmodel.MyOrderViewModel

class FragmentMyOrder(private val myOrderType: MyOrderType) : BaseFragmentVM<FragmentMyOrderBinding, MyOrderViewModel>(
    R.layout.fragment_my_order,
    MyOrderViewModel::class
) {
    private val myOrderRecyclerViewAdapter = MyOrderRecyclerViewAdapter(myOrderType)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = myOrderRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        when(myOrderType) {
            MyOrderType.PENDING -> {}
            MyOrderType.COMPLETED -> viewModel.getCompletedOrderList()
        }
    }

    override fun observeData() {
        super.observeData()

        viewDataBinding.run {
            viewModel.completedOrderList.observe(requireActivity(), {
                myOrderRecyclerViewAdapter.addData(it.order_list)
            })
        }
    }

    override fun initListener() {
        viewDataBinding.run {
            swipeRefreshLayout.setOnRefreshListener {
                when(myOrderType) {
                    MyOrderType.PENDING -> {}
                    MyOrderType.COMPLETED -> viewModel.getCompletedOrderList()
                }
            }
        }
    }
}