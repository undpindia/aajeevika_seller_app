package com.aajeevika.clf.view.application.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemLoadMoreBinding
import com.aajeevika.clf.databinding.ListItemRatingAndReviewBinding
import com.aajeevika.clf.model.data_model.CategoryData
import com.aajeevika.clf.model.data_model.RatingListData

class RatingAndReviewsRecyclerViewAdapter(private val requestData: (Int) -> Unit) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<RatingListData>()
    private var isNextPageRequested = false
    private var currentPage: Int = -1
    private var lastPage: Int = -1

    fun addData(data: ArrayList<RatingListData>, currentPage: Int, lastPage: Int) {
        isNextPageRequested = false

        this.currentPage = currentPage
        this.lastPage = lastPage

        if(currentPage == 1) dataList.clear()
        val currentDataLisSize = dataList.size
        dataList.addAll(data)

        if(currentPage == 1) notifyDataSetChanged()
        else notifyItemRangeChanged(currentDataLisSize, dataList.size - currentDataLisSize)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisibleItemPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()

                if(lastVisibleItemPosition == dataList.size && !isNextPageRequested) {
                    isNextPageRequested = true
                    requestData(currentPage + 1)
                }
            }
        })
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder? = run {
        when(viewType) {
            1 -> RatingAndReviewViewHolder(ListItemRatingAndReviewBinding.inflate(inflater, parent, false))
            0 -> LoadMoreViewHolder(ListItemLoadMoreBinding.inflate(inflater, parent, false))
            else -> null
        }
    }

    override fun getItemCount() = dataList.size + if(currentPage < lastPage) 1 else 0

    override fun getItemViewType(position: Int) = if(position < dataList.size) 1 else 0

    inner class RatingAndReviewViewHolder(private val viewDataBinding: ListItemRatingAndReviewBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                userName = data.getreviews.name
                review = data.review_msg
                rating = data.rating

                if(data.getreviews.profileImage?.isNotEmpty() == true)
                    profileImage = BaseUrls.baseUrl + data.getreviews.profileImage
            }
        }
    }

    private inner class LoadMoreViewHolder(private val viewDataBinding: ListItemLoadMoreBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {}
    }
}