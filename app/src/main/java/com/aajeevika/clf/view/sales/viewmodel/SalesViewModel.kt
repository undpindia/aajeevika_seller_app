package com.aajeevika.clf.view.sales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.SalesListData
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class SalesViewModel : BaseViewModel() {
    private val _salesLiveData = MutableLiveData<SalesListData>()
    val salesLiveData: LiveData<SalesListData> = _salesLiveData

    fun getSalesList(type: String, page: Int = 1) {
        val requestMap = HashMap<String, Any>()
        requestMap["page"] = page
        requestMap["order_type"] = type

        requestData(
            { apiService.getSales(requestMap) },
            { it.data?.run { _salesLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}