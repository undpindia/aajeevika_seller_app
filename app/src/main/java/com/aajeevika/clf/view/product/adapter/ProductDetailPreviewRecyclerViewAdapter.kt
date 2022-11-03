package com.aajeevika.clf.view.product.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemProductDetailsPreviewBinding
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.app_enum.ProductMediaType
import com.aajeevika.clf.view.application.ActivityVideoPlayer
import com.bumptech.glide.util.Util
import java.util.*
import kotlin.collections.ArrayList

class ProductDetailPreviewRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<Pair<ProductMediaType, String>>()

    fun addData(data: ArrayList<Pair<ProductMediaType, String>>) {
        this.dataList.clear()
        this.dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductDetailPreviewViewHolder(ListItemProductDetailsPreviewBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class ProductDetailPreviewViewHolder(private val viewDataBinding: ListItemProductDetailsPreviewBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                youtubeImg.visibility = if(data.first == ProductMediaType.VIDEO) View.VISIBLE else View.GONE

                image = when (data.first) {
                    ProductMediaType.VIDEO -> UtilityActions.getVideoThumbnail(data.second)
                    ProductMediaType.IMAGE -> BaseUrls.baseUrl + data.second
                }

                root.setOnClickListener {
                    if(data.first == ProductMediaType.VIDEO) {
                        val intent = Intent(context, ActivityVideoPlayer::class.java)
                        intent.putExtra(Constant.WEB_URL, data.second)
                        context.startActivity(intent)
                    } else if(adapterPosition != 0) {
                        Collections.swap(dataList, 0, adapterPosition)
                        notifyItemChanged(adapterPosition)
                        notifyItemChanged(0)
                    }
                }
            }
        }
    }
}