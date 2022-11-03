package com.aajeevika.clf.view.buy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.BuyManagerDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class BuyManagerViewModel : BaseViewModel() {
    private val _salesListLiveData = MutableLiveData<BuyManagerDataModel>()
    val salesListLiveData: LiveData<BuyManagerDataModel> = _salesListLiveData

    fun getIndividualSalesList() {
        requestData(
            { apiService.getIndividualSalesList() },
            { it.data?.run { _salesListLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}