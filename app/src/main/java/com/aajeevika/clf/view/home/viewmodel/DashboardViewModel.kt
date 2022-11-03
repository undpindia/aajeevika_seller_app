package com.aajeevika.clf.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.HomeDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class DashboardViewModel : BaseViewModel() {
    private val _homeLiveData = MutableLiveData<HomeDataModel>()
    val homeLiveData: LiveData<HomeDataModel> = _homeLiveData

    fun getHomeData(page: Int = 1) {
        val requestMap = HashMap<String, Any>()
        requestMap["page"] = page

        requestData(
            { apiService.getHomeData(requestMap) },
            { it.data.run { _homeLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}