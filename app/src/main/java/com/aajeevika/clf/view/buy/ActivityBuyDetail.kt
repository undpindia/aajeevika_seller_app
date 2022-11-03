package com.aajeevika.clf.view.buy

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityBuyDetailBinding
import com.aajeevika.clf.model.data_model.BuyDetailDataModel
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.view.buy.adapter.BuyDetailProductsRecyclerViewAdapter
import com.aajeevika.clf.view.buy.viewmodel.BuyDetailViewModel

class ActivityBuyDetail : BaseActivityVM<ActivityBuyDetailBinding, BuyDetailViewModel>(
    R.layout.activity_buy_detail,
    BuyDetailViewModel::class
) {
    private val buyDetailProductsRecyclerViewAdapter = BuyDetailProductsRecyclerViewAdapter()
    private val orderId by lazy { intent.getIntExtra(Constant.ORDER_ID, -1) }
    private lateinit var buyDetailDataModel: BuyDetailDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = buyDetailProductsRecyclerViewAdapter
        }

        viewModel.getIndividualSaleDetails(orderId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.rateStatusLiveData.observe(this, {
            intent.putExtra(Constant.ORDER_ID, orderId)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        })

        viewModel.saleDetailLiveData.observe(this, {
            buyDetailProductsRecyclerViewAdapter.addData(it.order_details.ind_items)
            buyDetailDataModel = it

            viewDataBinding.run {
                buyerName = it.order_details.get_individual.name
                buyerId = it.order_details.order_id_d

                if(it.order_details.created_at.isNotEmpty()) {
                    UtilityActions.parseInterestDate(it.order_details.created_at)?.let { updateDate ->
                        date = UtilityActions.formatInterestDate(updateDate)
                    }
                }

                //ind_rating indicates that the clf rated individual. So, don't replace "ind_rating" with "clf_rating"
                clfProfileImage = it.order_details.get_clf.profileImage?.run { BaseUrls.baseUrl + this }
                clfName = it.order_details.get_clf.name
                clfRating = it.order_details.ind_rating?.rating ?: 0F
                clfMessage = it.order_details.ind_rating?.review_msg

                //clf_rating indicates that the individual rated clf. So, don't replace "clf_rating" with "ind_rating"
                individualProfileImage = it.order_details.get_individual.profileImage?.run { BaseUrls.baseUrl + this }
                individualName = it.order_details.get_individual.name
                individualRating = it.order_details.clf_rating?.rating ?: 0F
                individualMessage = it.order_details.clf_rating?.review_msg

                executePendingBindings()
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            submitBtn.setOnClickListener {
                val review = inputReview.text.toString().trim()
                val rating = ratingBar.rating

                validateFormData(rating, review)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    if(::buyDetailDataModel.isInitialized) {
                        viewModel.addRatingToSale(buyDetailDataModel.order_details.id, buyDetailDataModel.order_details.get_individual.id, rating, review)
                    }
                }
            }
        }
    }

    private fun validateFormData(rating: Float, review: String): String? {
        return when {
            review.isEmpty() -> getString(R.string.please_add_a_review_for_this_sale)
            rating == 0F -> getString(R.string.please_rate_this_sale)
            else -> null
        }
    }
}