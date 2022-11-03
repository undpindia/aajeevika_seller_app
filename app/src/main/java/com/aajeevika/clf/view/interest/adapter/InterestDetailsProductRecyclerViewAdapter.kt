package com.aajeevika.clf.view.interest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemInterestsDetailsProductBinding
import com.aajeevika.clf.model.data_model.InterestItems

class InterestDetailsProductRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<InterestItems>()

    fun addData(data: ArrayList<InterestItems>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductViewHolder(ListItemInterestsDetailsProductBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ProductViewHolder(private val viewDataBinding: ListItemInterestsDetailsProductBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val product = dataList[adapterPosition]

            viewDataBinding.run {
                name = product.product.name
                quantity = product.quantity.toString()
                price = (product.quantity * product.product.price).toString()
                priceUnit = product.product.price_unit
            }
        }
    }
}