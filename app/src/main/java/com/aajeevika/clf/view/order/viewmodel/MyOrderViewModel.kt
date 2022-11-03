package com.aajeevika.clf.view.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.MyOrdersListData
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class MyOrderViewModel : BaseViewModel() {
    private val _completedOrderList = MutableLiveData<MyOrdersListData>()
    val completedOrderList: LiveData<MyOrdersListData> = _completedOrderList

    fun getCompletedOrderList() {
        requestData(
            { apiService.getCompletedOrderList() },
            { it.data?.run { _completedOrderList.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}