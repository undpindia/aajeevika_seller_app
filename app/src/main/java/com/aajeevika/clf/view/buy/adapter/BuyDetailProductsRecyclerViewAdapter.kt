package com.aajeevika.clf.view.buy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemBuyDetailsProductBinding
import com.aajeevika.clf.model.data_model.BuyDetailItemData

class BuyDetailProductsRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<BuyDetailItemData>()

    fun addData(data: ArrayList<BuyDetailItemData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        BuyDetailProductViewHolder(ListItemBuyDetailsProductBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class BuyDetailProductViewHolder(val dataBinding: ListItemBuyDetailsProductBinding): BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                name = data.indproduct.name_en
                quantity = data.quantity.toString()
                priceUnit = data.indproduct.price_unit
            }
        }
    }
}