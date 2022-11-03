package com.aajeevika.clf.view.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.ProductDraftResponse
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class DraftViewModel : BaseViewModel() {
    private val _draftLiveData = MutableLiveData<ProductDraftResponse>()
    val draftLiveData: LiveData<ProductDraftResponse> = _draftLiveData

    private val _statusLiveData = MutableLiveData<String>()
    val statusLiveData: LiveData<String> = _statusLiveData

    fun getDraftDetails() {
        requestData(
            { apiService.getDraftProduct() },
            { it.data?.run { _draftLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.MESSAGE,
        )
    }

    fun removeDraft(id: Int) {
        val map = HashMap<String, Any>()
        map["productId"] = id

        requestData(
            { apiService.removeDraftProduct(map) },
            { it.message?.run{ _statusLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}