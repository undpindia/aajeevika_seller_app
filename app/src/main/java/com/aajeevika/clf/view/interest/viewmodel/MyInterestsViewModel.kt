package com.aajeevika.clf.view.interest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.Interest
import com.aajeevika.clf.model.data_model.InterestListDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class MyInterestsViewModel : BaseViewModel() {
    private val _myInterestsLiveData = MutableLiveData<InterestListDataModel>()
    val myInterestsLiveData: LiveData<InterestListDataModel> = _myInterestsLiveData

    private val _interestsDetailLiveData = MutableLiveData<Interest>()
    val interestsDetailLiveData: LiveData<Interest> = _interestsDetailLiveData

    fun getMyInterests() {
        requestData(
            { apiService.getMyInterests() },
            { it.data?.run { _myInterestsLiveData.postValue(this) }},
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }

    fun getInterestById(id: Int) {
        val map = HashMap<String, Any>()
        map["interestId"] = id

        requestData(
            { apiService.getInterestById(map) },
            { it.data?.seller_interest?.run { _interestsDetailLiveData.postValue(this[0]) }},
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}