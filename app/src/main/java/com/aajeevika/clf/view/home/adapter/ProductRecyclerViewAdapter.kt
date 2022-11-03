package com.aajeevika.clf.view.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemProductCardBinding
import com.aajeevika.clf.model.data_model.ProductData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.view.product.ActivityProductDetail

class ProductRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<ProductData>()

    fun addData(data: ArrayList<ProductData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductViewHolder(ListItemProductCardBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ProductViewHolder(private val viewDataBinding: ListItemProductCardBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val product = dataList[adapterPosition]

            viewDataBinding.run {
                productImage = BaseUrls.baseUrl + product.image_1
                productId = product.product_id_d
                amount = product.price
                title = "${ product.template.name } (${ product.name })"

                root.layoutParams.width = (UtilityActions.windowWidth() * .41).toInt()

                root.setOnClickListener {
                    val intent = Intent(context, ActivityProductDetail::class.java)
                    intent.putExtra(Constant.PRODUCT_ID, product.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}