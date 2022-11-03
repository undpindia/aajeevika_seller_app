package com.aajeevika.clf.view.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.SubCategoryDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class SubCategoryViewModel : BaseViewModel() {
    private val _subCategoryLiveData = MutableLiveData<SubCategoryDataModel>()
    val subCategoryLiveData: LiveData<SubCategoryDataModel> = _subCategoryLiveData

    fun getSubCategory(uid: Int, subCategoryId: Int, page: Int = 1) {
        val map = HashMap<String, Any>()
        map["artisanshgid"] = uid
        map["subcategoryId"] = subCategoryId
        map["page"] = page

        requestData(
            { apiService.getSubCategoryProducts(map) },
            { it.data.run { _subCategoryLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}