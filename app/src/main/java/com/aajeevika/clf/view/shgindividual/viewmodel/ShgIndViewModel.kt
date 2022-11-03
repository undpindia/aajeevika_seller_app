package com.aajeevika.clf.view.shgindividual.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.ShgIndDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class ShgIndViewModel : BaseViewModel() {
    private val _shgListLiveData = MutableLiveData<ShgIndDataModel>()
    val shgListLiveData: LiveData<ShgIndDataModel> = _shgListLiveData

    fun getIndividualSalesList() {
        requestData(
            { apiService.getShgIndList() },
            { it.data?.run { _shgListLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}