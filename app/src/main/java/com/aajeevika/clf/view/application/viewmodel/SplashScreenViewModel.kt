package com.aajeevika.clf.view.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.UserProfileModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class SplashScreenViewModel : BaseViewModel() {
    private val _userLiveData = MutableLiveData<UserProfileModel>()
    val userLiveData: LiveData<UserProfileModel> = _userLiveData

    fun requestUserProfile() {
        requestData(
            { apiService.getProfile() },
            { it.data?.run { _userLiveData.postValue(this) }},
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.LOGOUT,
        )
    }
}