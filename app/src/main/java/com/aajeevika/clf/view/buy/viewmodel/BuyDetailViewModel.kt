package com.aajeevika.clf.view.buy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.BuyDetailDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class BuyDetailViewModel : BaseViewModel() {
    private val _saleDetailLiveData = MutableLiveData<BuyDetailDataModel>()
    val saleDetailLiveData: LiveData<BuyDetailDataModel> = _saleDetailLiveData

    private val _rateStatusLiveData = MutableLiveData<Boolean>()
    val rateStatusLiveData: LiveData<Boolean> = _rateStatusLiveData

    fun getIndividualSaleDetails(id: Int) {
        val requestMap = hashMapOf<String, Any>(Pair("order_id", id))

        requestData(
            { apiService.getIndividualSaleDetails(requestMap) },
            { it.data?.run { _saleDetailLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }

    fun addRatingToSale(orderId: Int, userId : Int, rating: Float, review: String) {
        val requestMap = hashMapOf<String, Any>(
            Pair("rating", rating),
            Pair("user_id", userId),
            Pair("order_id", orderId),
            Pair("review_msg", review)
        )

        requestData(
            { apiService.addRatingToSale(requestMap) },
            { _rateStatusLiveData.postValue(it.status) },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.MESSAGE,
        )
    }
}