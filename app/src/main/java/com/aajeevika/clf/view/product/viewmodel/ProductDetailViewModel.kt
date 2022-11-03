package com.aajeevika.clf.view.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.ProductDetailDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class ProductDetailViewModel : BaseViewModel() {
    private val _productDetailLiveData = MutableLiveData<ProductDetailDataModel>()
    val productDetailLiveData: LiveData<ProductDetailDataModel> = _productDetailLiveData

    fun getProductDetails(productId: Int) {
        val map = HashMap<String, Any>()
        map["productId"] = productId

        requestData(
            { apiService.getProductDetails(map) },
            { it.data?.run { _productDetailLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}