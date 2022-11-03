package com.aajeevika.clf.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.SearchResultData
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class SearchViewModel : BaseViewModel() {
    private val _searchLiveData = MutableLiveData<SearchResultData>()
    val searchLiveData: LiveData<SearchResultData> = _searchLiveData

    fun searchProduct(key: String, page: Int = 1) {
        val requestMap = HashMap<String, Any>()
        requestMap["keyword"] = key
        requestMap["page"] = page

        requestData(
            { apiService.searchProduct(requestMap) },
            { it.data?.products?.run { _searchLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}