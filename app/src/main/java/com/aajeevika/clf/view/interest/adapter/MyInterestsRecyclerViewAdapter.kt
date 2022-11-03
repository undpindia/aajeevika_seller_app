package com.aajeevika.clf.view.interest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemMyInterestsBinding
import com.aajeevika.clf.model.data_model.InterestData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.view.interest.ActivityInterestDetails

class MyInterestsRecyclerViewAdapter(private val isToAddSale: Boolean, private val onSelect: (Int) -> Unit) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<InterestData>()

    fun addData(data: ArrayList<InterestData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run{
        MyInterestsViewHolder(ListItemMyInterestsBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class MyInterestsViewHolder(private val viewDataBinding: ListItemMyInterestsBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                interestId = data.interest_Id
                buyerName = data.buyer.name

                productImage = if(!data.buyer.profileImage.isNullOrEmpty()) BaseUrls.baseUrl + data.buyer.profileImage else null

                if(data.items.isNotEmpty()) {
                    UtilityActions.parseInterestDate(data.items[0].created_at)?.let {
                        date = UtilityActions.formatInterestDate(it)
                    }
                }

                root.setOnClickListener {
                    if(isToAddSale) {
                        onSelect(data.id)
                    } else {
                        val intent = Intent(context, ActivityInterestDetails::class.java)
                        intent.putExtra(Constant.INTEREST_ID, data.id)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}