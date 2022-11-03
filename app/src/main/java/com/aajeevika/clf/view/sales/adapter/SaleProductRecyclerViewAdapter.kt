package com.aajeevika.clf.view.sales.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemSaleProductBinding
import com.aajeevika.clf.model.data_model.SalesSellerProducts

class SaleProductRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private lateinit var dataList: ArrayList<SalesSellerProducts>

    fun addData(data: ArrayList<SalesSellerProducts>) {
        dataList = data
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        NewSaleProductViewHolder(ListItemSaleProductBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = if(::dataList.isInitialized) dataList.size else 0

    private inner class NewSaleProductViewHolder(private val viewDataBinding: ListItemSaleProductBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                name = data.name
                price = data.price
                quantity = data.quantity.toString()
                priceUnit = data.price_unit

                deleteBtn.setOnClickListener {
                    dataList.removeAt(adapterPosition)
                    notifyDataSetChanged()
                }
            }
        }
    }
}