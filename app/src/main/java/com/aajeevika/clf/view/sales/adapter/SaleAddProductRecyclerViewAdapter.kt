package com.aajeevika.clf.view.sales.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemSaleAddProductBinding
import com.aajeevika.clf.model.data_model.SalesSellerProducts
import com.aajeevika.clf.utility.UtilityActions

class SaleAddProductRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SalesSellerProducts>()

    fun addData(data: ArrayList<SalesSellerProducts>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun getDataList() = dataList.filter { it.quantity > 0 }.toCollection(ArrayList())

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        AddSaleProductViewHolder(ListItemSaleAddProductBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class AddSaleProductViewHolder(private val viewDataBinding: ListItemSaleAddProductBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                availableQuantity = data.qty.toString()
                productImage = BaseUrls.baseUrl + data.image_1
                quantity = data.quantity.toString()
                priceUnit = data.price_unit
                productName = data.name
                price = data.price

                removeBtn.setOnClickListener {
                    if(data.quantity > 0) {
                        UtilityActions.closeKeyboard(context, qtyValue)

                        data.quantity--
                        quantity = data.quantity.toString()
                        executePendingBindings()
                    }
                }

                addBtn.setOnClickListener {
                    if(data.quantity < data.qty) {
                        UtilityActions.closeKeyboard(context, qtyValue)

                        data.quantity++
                        quantity = data.quantity.toString()
                        executePendingBindings()
                    }
                }

                /*qtyValue.doOnTextChanged { text, _, _, _ ->
                    val enteredQuantity = if(text.toString().trim().isEmpty()) 0 else text.toString().trim().toInt()
                    if(enteredQuantity > data.qty) {
                        data.quantity = data.qty
                        qtyValue.setText(data.qty.toString())
                        qtyValue.setSelection(data.qty.toString().trim().length)
                    } else {
                        data.quantity = enteredQuantity
                    }
                }*/
            }
        }
    }
}