package com.aajeevika.clf.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.OtpModel
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RegisterViewModel : BaseViewModel() {
    private val _otpLiveData = MutableLiveData<OtpModel>()
    val otpLiveData: LiveData<OtpModel> = _otpLiveData

    fun requestUserRegistration(roleId: Int, profile: File, nameOfOrganization: String, memberName: String, memberDesignation: String, memberId: String, email: String, mobileNumber: String, password: String) {
        val requestMap = HashMap<String, RequestBody>()
        requestMap["role_id"] = roleId.toString().toRequestBody()
        requestMap["email"] = email.toRequestBody()
        requestMap["name"] = memberName.toRequestBody()
        requestMap["password"] = password.toRequestBody()
        requestMap["mobile"] = mobileNumber.toRequestBody()
        requestMap["organization_name"] = nameOfOrganization.toRequestBody()
        requestMap["member_designation"] = memberDesignation.toRequestBody()
        if(memberId.isNotEmpty()) requestMap["member_id"] = memberId.toRequestBody()

        requestData(
            { apiService.registerUser(requestMap, UtilityActions.multipartImage(profile, "profileimage")) },
            { it.data?.run { _otpLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }
}