package com.aajeevika.clf.view.application

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityRatingAndReviewsBinding
import com.aajeevika.clf.view.application.adapter.RatingAndReviewsRecyclerViewAdapter
import com.aajeevika.clf.view.application.viewmodel.RatingAndReviewsViewModel

class ActivityRatingAndReviews : BaseActivityVM<ActivityRatingAndReviewsBinding, RatingAndReviewsViewModel>(
    R.layout.activity_rating_and_reviews,
    RatingAndReviewsViewModel::class
) {
    private val ratingAndReviewsRecyclerViewAdapter = RatingAndReviewsRecyclerViewAdapter {
        viewModel.getReviews(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = ratingAndReviewsRecyclerViewAdapter

        viewModel.getReviews()
    }

    override fun observeData() {
        super.observeData()

        viewModel.reviewLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            ratingAndReviewsRecyclerViewAdapter.addData(it.ratings, it.pagination.current_page, it.pagination.last_page)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getReviews()
            }
        }
    }
}