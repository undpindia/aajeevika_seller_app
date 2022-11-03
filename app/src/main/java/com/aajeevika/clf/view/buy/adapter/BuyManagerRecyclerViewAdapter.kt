package com.aajeevika.clf.view.buy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemBuyManagerBinding
import com.aajeevika.clf.model.data_model.BuyManagerOrderData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.view.buy.ActivityBuyDetail

class BuyManagerRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<BuyManagerOrderData>()

    fun addData(data: ArrayList<BuyManagerOrderData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        OrderViewHolder(ListItemBuyManagerBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class OrderViewHolder(val dataBinding: ListItemBuyManagerBinding) : BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                name = data.get_individual.name
                buyerId = data.order_id_d

                if(data.created_at.isNotEmpty()) {
                    UtilityActions.parseInterestDate(data.created_at)?.let { updateDate ->
                        date = UtilityActions.formatInterestDate(updateDate)
                    }
                }

                root.setOnClickListener {
                    val intent = Intent(context, ActivityBuyDetail::class.java)
                    intent.putExtra(Constant.ORDER_ID, data.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}