package com.aajeevika.clf.view.shgindividual.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemShgIndividualsBinding
import com.aajeevika.clf.model.data_model.IndividualData
import java.lang.StringBuilder

class ShgIndRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<IndividualData>()

    fun addData(data: ArrayList<IndividualData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ShgIndViewHolder(ListItemShgIndividualsBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ShgIndViewHolder(val dataBinding: ListItemShgIndividualsBinding) : BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                val addressBuilder = StringBuilder()
                data.address_line_one?.run { addressBuilder.append("$this, ") }
                data.address_line_two?.run { addressBuilder.append("$this, ") }
                data.block?.run { addressBuilder.append("$this, ") }
                data.district?.run { addressBuilder.append("$this, ") }
                data.state?.run { addressBuilder.append("$this, ") }
                data.country?.run { addressBuilder.append("$this, ") }
                data.pincode?.run { addressBuilder.append(this) }

                name = data.name
                email = data.email
                phone = data.mobile
                address = addressBuilder.toString()
                rating = data.ratingAvgStar ?: 0F
            }
        }
    }
}