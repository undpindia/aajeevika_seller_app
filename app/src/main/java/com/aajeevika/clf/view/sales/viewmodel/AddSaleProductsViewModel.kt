package com.aajeevika.clf.view.sales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.SalesSellerProductsDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class AddSaleProductsViewModel : BaseViewModel() {
    private val _productsLiveData = MutableLiveData<SalesSellerProductsDataModel>()
    val productsLiveData: LiveData<SalesSellerProductsDataModel> = _productsLiveData

    fun getInterestById(id: Int) {
        val map = HashMap<String, Any>()
        map["sellerId"] = id

        requestData(
            { apiService.getSellerProducts(map) },
            { it.data?.run { _productsLiveData.postValue(this) }},
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}