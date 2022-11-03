package com.aajeevika.clf.view.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemOrderDetailsProductBinding
import com.aajeevika.clf.model.data_model.OrderDetailsItemsListData

class OrderDetailsProductRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<OrderDetailsItemsListData>()

    fun addData(data: ArrayList<OrderDetailsItemsListData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun getTotalPrice() : Int {
        var totalPrice = 0

        dataList.forEach { totalPrice += it.quantity * it.product_price }
        return totalPrice
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductViewHolder(ListItemOrderDetailsProductBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ProductViewHolder(private val viewDataBinding: ListItemOrderDetailsProductBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                val product = dataList[adapterPosition]

                viewDataBinding.run {
                    name = product.product.name
                    quantity = product.quantity.toString()
                    price = (product.quantity * product.product_price).toString()
                    priceUnit = product.product.price_unit
                }
            }
        }
    }
}