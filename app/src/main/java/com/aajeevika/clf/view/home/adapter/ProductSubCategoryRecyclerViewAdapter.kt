package com.aajeevika.clf.view.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemProductSubCategoryListBinding
import com.aajeevika.clf.model.data_model.SubCategoryData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.view.product.ActivitySubCategory

class ProductSubCategoryRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SubCategoryData>()

    fun addData(data: ArrayList<SubCategoryData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductSubCategoryViewHolder(ListItemProductSubCategoryListBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ProductSubCategoryViewHolder(private val viewDataBinding: ListItemProductSubCategoryListBinding) : BaseViewHolder(viewDataBinding) {
        private val productRecyclerViewAdapter = ProductRecyclerViewAdapter()

        override fun bindData(context: Context) {
            val subCategoryData = dataList[adapterPosition]

            viewDataBinding.run {
                title = subCategoryData.subCategoryName
                productRecyclerViewAdapter.addData(subCategoryData.products)

                recyclerView.run {
                    adapter = productRecyclerViewAdapter

                    if(itemDecorationCount == 0)
                        addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
                }

                viewAllBtn.setOnClickListener {
                    val intent = Intent(context, ActivitySubCategory::class.java)
                    intent.putExtra(Constant.SUB_CATEGORY_ID, subCategoryData.subCategoryId)
                    intent.putExtra(Constant.TITLE, subCategoryData.subCategoryName)
                    context.startActivity(intent)
                }
            }
        }
    }
}