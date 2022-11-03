package com.aajeevika.clf.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.OtpModel
import com.aajeevika.clf.model.data_model.UserProfileModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction

class OtpVerificationViewModel : BaseViewModel() {
    private val _userProfileLiveData = MutableLiveData<UserProfileModel>()
    val userProfileLiveData: LiveData<UserProfileModel> = _userProfileLiveData

    private val _otpLiveData = MutableLiveData<OtpModel>()
    val otpLiveData: LiveData<OtpModel> = _otpLiveData

    private val _updateMobileLiveData = MutableLiveData<String>()
    val updateMobileLiveData: LiveData<String> = _updateMobileLiveData

    fun requestOtpVerification(mobileNo: String, otp: String, isSignUp: Boolean) {
        val map = HashMap<String, Any>()
        map["mobile"] = mobileNo
        map["otp"] = otp
        if (isSignUp) map["type"] = "signup"

        requestData(
            { apiService.verifyOtp(map) },
            { it.data.run { _userProfileLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun requestOtpResend(mobileNo: String, isChangeMobile: Boolean = false) {
        val map = HashMap<String, Any>()

        map["mobile"] = mobileNo
        if (isChangeMobile) map["type"] = "updatemobile"

        requestData(
            { apiService.resendOtp(map) },
            { it.data?.run { _otpLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun updateMobileNumber(mobileNo: String, otp: Int) {
        val map = HashMap<String, Any>()
        map["mobile"] = mobileNo
        map["otp"] = otp

        requestData(
            { apiService.updateMobileNumber(map) },
            { it.message?.run { _updateMobileLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}