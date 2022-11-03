package com.aajeevika.clf.view.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.RatingDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class RatingAndReviewsViewModel : BaseViewModel() {
    private val _reviewLiveData = MutableLiveData<RatingDataModel>()
    val reviewLiveData: LiveData<RatingDataModel> = _reviewLiveData

    fun getReviews(page: Int = 1) {
        val requestMap = HashMap<String, Any>()
        requestMap["page"] = page

        requestData(
            { apiService.getReviews(requestMap) },
            { it.data?.run { _reviewLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}