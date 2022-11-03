package com.aajeevika.clf.view.sales.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemLoadMoreBinding
import com.aajeevika.clf.databinding.ListItemSalesBinding
import com.aajeevika.clf.model.data_model.SalesData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.view.order.ActivityOrderDetailsCompleted

class SalesRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SalesData>()

    fun addData(data: ArrayList<SalesData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        SalesViewHolder(ListItemSalesBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class SalesViewHolder(private val viewDataBinding: ListItemSalesBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                name = data.buyer.name
                orderId = data.order_id_d

                if(data.items.isNotEmpty()) {
                    UtilityActions.parseInterestDate(data.items[0].updated_at)?.let { updateDate ->
                        date = UtilityActions.formatInterestDate(updateDate)
                    }
                }

                root.setOnClickListener {
                    val intent = Intent(context, ActivityOrderDetailsCompleted::class.java)
                    intent.putExtra(Constant.ORDER_ID, data.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}