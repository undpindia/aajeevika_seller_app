package com.aajeevika.clf.view.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.BlockData
import com.aajeevika.clf.model.data_model.BlockModel
import com.aajeevika.clf.model.data_model.CityData
import com.aajeevika.clf.model.data_model.UserProfileModel
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction
import okhttp3.MultipartBody

class ProfileViewModel : BaseViewModel() {
    private val _userLiveData = MutableLiveData<UserProfileModel>()
    val userLiveData: LiveData<UserProfileModel> = _userLiveData

    private val _statusLiveData = MutableLiveData<String>()
    val statusLiveData: LiveData<String> = _statusLiveData

    private val _districtLiveData = MutableLiveData<ArrayList<CityData>>()
    val districtLiveData: LiveData<ArrayList<CityData>> = _districtLiveData

    private val _blockLiveData = MutableLiveData<ArrayList<BlockData>>()
    val blockLiveData: LiveData<ArrayList<BlockData>> = _blockLiveData

    private val _updateAddressStatusLiveData = MutableLiveData<String>()
    val updateAddressStatusLiveData: LiveData<String> = _updateAddressStatusLiveData

    fun requestUserProfile() {
        requestData(
            { apiService.getProfile() },
            { it.data?.run { _userLiveData.postValue(this) }},
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun uploadProfileImage(file: MultipartBody.Part) {
        requestData(
            { apiService.uploadProfileImage(file) },
            { it.message?.run { _statusLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun updateProfileData(name: String, memberId: String, designation: String, organization: String, email: String) {
        val requestMap = HashMap<String, Any>()
        requestMap["name"] = name
        requestMap["member_id"] = memberId
        requestMap["organization_name"] = organization
        requestMap["member_designation"] = designation
        requestMap["email"] = email

        requestData(
            { apiService.updateUserProfile(requestMap) },
            { it.message?.run { _statusLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun getDistrict() {
        val map = HashMap<String, Any>()
        map["state_id"] = Constant.DEFAULT_STATE_ID

        requestData(
            { apiService.getCity(map) },
            { it.data?.district?.run { _districtLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.NONE,
        )
    }

    fun getBlock(id: Int) {
        val map = HashMap<String, Any>()
        map["city_id"] = id

        requestData(
            { apiService.getBlock(map) },
            { it.data?.block?.run { _blockLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.NONE,
        )
    }

    fun updateAddress(addressId: Int, addressLineOne: String, addressLineTwo: String, countryId: Int, stateId: Int, districtId: Int, blockId: Int, pin: Int, lat: Double, long: Double) {
        val requestMap = HashMap<String, Any>()
        requestMap["id"] = addressId
        requestMap["address_line_one"] = addressLineOne
        if(addressLineTwo.isNotEmpty()) requestMap["address_line_two"] = addressLineTwo
        requestMap["country"] = countryId
        requestMap["state"] = stateId
        requestMap["district"] = districtId
        requestMap["pincode"] = pin
        requestMap["address_type"] = "registered"
        requestMap["block"] = blockId
        requestMap["lat"] = lat
        requestMap["log"] = long

        requestData(
            { apiService.updateAddress(requestMap) },
            { it.message?.run { _updateAddressStatusLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}