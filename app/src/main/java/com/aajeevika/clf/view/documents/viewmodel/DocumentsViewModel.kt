package com.aajeevika.clf.view.documents.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.BlockData
import com.aajeevika.clf.model.data_model.CityData
import com.aajeevika.clf.model.data_model.CountryData
import com.aajeevika.clf.model.data_model.StateData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class DocumentsViewModel : BaseViewModel() {
    private val _countryLiveData = MutableLiveData<ArrayList<CountryData>>()
    val countryLiveData: LiveData<ArrayList<CountryData>> = _countryLiveData

    private val _stateLiveData = MutableLiveData<ArrayList<StateData>>()
    val stateLiveData: LiveData<ArrayList<StateData>> = _stateLiveData

    private val _districtLiveData = MutableLiveData<ArrayList<CityData>>()
    val districtLiveData: LiveData<ArrayList<CityData>> = _districtLiveData

    private val _blockLiveData = MutableLiveData<ArrayList<BlockData>>()
    val blockLiveData: LiveData<ArrayList<BlockData>> = _blockLiveData

    private val _requestStatusLiveData = MutableLiveData<String>()
    val requestStatusLiveData: LiveData<String> = _requestStatusLiveData

    fun getCountries() {
        requestData(
            { apiService.getCountries() },
            { it.data?.country?.run { _countryLiveData.postValue(this) } },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.NONE,
        )
    }

    fun getState() {
        val map = HashMap<String, Any>()
        map["country_id"] = Constant.DEFAULT_COUNTRY_ID

        requestData(
            { apiService.getState(map) },
            { it.data?.states?.run { _stateLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.NONE,
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

    fun uploadDocuments(roleId: Int, fields: HashMap<String, RequestBody>, files: ArrayList<MultipartBody.Part>) {
        fields["role_id"] = roleId.toString().toRequestBody()

        requestData(
            { apiService.addDocumentAndAddress(fields, files) },
            { it.message?.run { _requestStatusLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun reUploadDocument(roleId: Int, fields: HashMap<String, RequestBody>, files: ArrayList<MultipartBody.Part>) {
        fields["role_id"] = roleId.toString().toRequestBody()

        requestData(
            { apiService.reUploadDocument(fields, files) },
            { it.message?.run { _requestStatusLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}