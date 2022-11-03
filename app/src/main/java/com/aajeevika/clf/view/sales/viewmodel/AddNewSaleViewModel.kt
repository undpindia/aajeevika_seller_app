package com.aajeevika.clf.view.sales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.AddNewSaleResponse
import com.aajeevika.clf.model.data_model.BaseDataModel
import com.aajeevika.clf.model.data_model.Interest
import com.aajeevika.clf.model.data_model.SalesSellerProducts
import com.aajeevika.clf.model.data_model.CollectionCenterDataModel
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.util.*
import kotlin.collections.HashMap

class AddNewSaleViewModel : BaseViewModel() {
    private val _interestsDetailLiveData = MutableLiveData<Interest>()
    val interestsDetailLiveData: LiveData<Interest> = _interestsDetailLiveData

    private val _saleStatusLiveData = MutableLiveData<BaseDataModel<AddNewSaleResponse>>()
    val saleStatusLiveData: LiveData<BaseDataModel<AddNewSaleResponse>> = _saleStatusLiveData

    private val _collectionCenterLiveData = MutableLiveData<CollectionCenterDataModel>()
    val collectionCenterLiveData: LiveData<CollectionCenterDataModel> = _collectionCenterLiveData

    fun getInterestById(id: Int) {
        val map = HashMap<String, Any>()
        map["interestId"] = id

        requestData(
            { apiService.getInterestById(map) },
            { it.data?.seller_interest?.run { _interestsDetailLiveData.postValue(this[0]) }},
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }

    fun addNewSale(interestId: Int, userId: Int, modeOfDelivery: Int, collectionCenterId: Int, rating: Float, review: String, saleDate: String, productList: ArrayList<SalesSellerProducts>) {
        val products = JsonArray()
        productList.forEach {
            val jsonObject = JsonObject()
            jsonObject.addProperty("product_id", it.id)
            jsonObject.addProperty("quantity", it.quantity)
            jsonObject.addProperty("product_price", it.price)
            products.add(jsonObject)
        }

        val requestBody = JsonObject()
        requestBody.addProperty("mode_of_delivery", modeOfDelivery)
        requestBody.addProperty("interestId", interestId)
        requestBody.addProperty("user_id", userId)
        requestBody.addProperty("rating", rating)
        requestBody.addProperty("review_msg", review)
        requestBody.addProperty("type", "seller")
        requestBody.addProperty("sale_date", saleDate)
        requestBody.add("products", products)

        if(collectionCenterId > 0) requestBody.addProperty("collection_center_id", collectionCenterId)

        requestData(
            { apiService.addNewSale(requestBody) },
            { _saleStatusLiveData.postValue(it) },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun getCollectionCenterList() {
        requestData(
            { apiService.getCollectionCenterList() },
            { it.data?.run { _collectionCenterLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.TOAST,
        )
    }
}