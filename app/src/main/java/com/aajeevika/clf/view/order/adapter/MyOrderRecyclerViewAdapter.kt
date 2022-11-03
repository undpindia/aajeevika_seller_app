package com.aajeevika.clf.view.order.adapter

import BaseUrls
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemMyOrderBinding
import com.aajeevika.clf.model.data_model.MyOrderData
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.app_enum.MyOrderType
import com.aajeevika.clf.view.order.ActivityOrderDetailsCompleted
import com.aajeevika.clf.view.order.ActivityOrderDetailsPending

class MyOrderRecyclerViewAdapter(private val myOrderType: MyOrderType) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<MyOrderData>()

    fun addData(data: ArrayList<MyOrderData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        MyOrderViewHolder(ListItemMyOrderBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class MyOrderViewHolder(private val viewDataBinding: ListItemMyOrderBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                buyerName = data.buyer.name
                orderId = data.order_id_d

                if(data.items.isNotEmpty()) {
                    productImage = BaseUrls.baseUrl + data.items[0].product.image_1

                    UtilityActions.parseInterestDate(data.items[0].created_at)?.let { date ->
                        orderDate = UtilityActions.formatInterestDate(date)
                    }
                }

                root.setOnClickListener {
                    when(myOrderType) {
                        MyOrderType.PENDING -> {
                            val intent = Intent(context, ActivityOrderDetailsPending::class.java)
                            context.startActivity(intent)
                        }
                        MyOrderType.COMPLETED -> {
                            val intent = Intent(context, ActivityOrderDetailsCompleted::class.java)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}