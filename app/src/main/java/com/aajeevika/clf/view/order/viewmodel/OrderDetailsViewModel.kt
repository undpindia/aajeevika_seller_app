package com.aajeevika.clf.view.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.OrderDetailsData
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class OrderDetailsViewModel : BaseViewModel() {
    private val _orderDetailsLiveData = MutableLiveData<OrderDetailsData>()
    val orderDetailsLiveData: LiveData<OrderDetailsData> = _orderDetailsLiveData

    fun getCompletedOrderList(id: Int) {
        val requestMap = HashMap<String, Any>()
        requestMap["orderId"] = id

        requestData(
            { apiService.getOrderById(requestMap) },
            { it.data?.all_order?.get(0)?.run { _orderDetailsLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}
