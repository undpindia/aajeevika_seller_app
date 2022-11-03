package com.aajeevika.clf.view.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.clf.baseclasses.BaseViewModel
import com.aajeevika.clf.model.data_model.*
import com.aajeevika.clf.utility.app_enum.ErrorType
import com.aajeevika.clf.utility.app_enum.ProgressAction
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProductAddViewModel : BaseViewModel() {
    private val _categoryLiveData = MutableLiveData<ProductCategoryResponse>()
    val categoryLiveData: LiveData<ProductCategoryResponse> = _categoryLiveData

    private val _subCategoryLiveData = MutableLiveData<ProductCategoryResponse>()
    val subCategoryLiveData: LiveData<ProductCategoryResponse> = _subCategoryLiveData

    private val _materialLiveData = MutableLiveData<MaterialResponse>()
    val materialLiveData: LiveData<MaterialResponse> = _materialLiveData

    private val _templateLiveData = MutableLiveData<TemplateResponse>()
    val templateLiveData: LiveData<TemplateResponse> = _templateLiveData

    private val _responseLiveData = MutableLiveData<String>()
    val responseLiveData: LiveData<String> = _responseLiveData

    private val _certificateListLiveData = MutableLiveData<ArrayList<CertificateData>>()
    val certificateListLiveData: LiveData<ArrayList<CertificateData>> = _certificateListLiveData

    fun getCategoryList() {
        requestData(
            { apiService.getCategory() },
            { it.data?.run { _categoryLiveData.postValue(this) }},
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.TOAST,
        )
    }

    fun getSubCategoryList(categoryId: Int) {
        val requestMap = HashMap<String, Any>()
        requestMap["id"] = categoryId

        requestData(
            { apiService.getSubCategory(requestMap) },
            { it.data?.run { _subCategoryLiveData.postValue(this) }},
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.TOAST,
        )
    }

    fun getMaterials(subCategoryId: Int) {
        val requestMap = HashMap<String, Any>()
        requestMap["subcategoryId"] = subCategoryId

        requestData(
            { apiService.getMaterial(requestMap) },
            { it.data?.run { _materialLiveData.postValue(this) }},
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.TOAST,
        )
    }

    fun getTemplate(categoryId: Int, subCategoryId: Int, materialId: Int) {
        val requestMap = HashMap<String, Any>()
        requestMap["categoryId"] = categoryId
        requestMap["subcategoryId"] = subCategoryId
        requestMap["materialId"] = materialId

        requestData(
            { apiService.getTemplate(requestMap) },
            { it.data?.run { _templateLiveData.postValue(this) }},
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.TOAST,
        )
    }

    fun getCertificateTypeList() {
        requestData(
            { apiService.getCertificateTypeList() },
            { it.data?.type_list?.run { _certificateListLiveData.postValue(this) }},
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.TOAST,
        )
    }

    fun addProduct(fields: AddProductRequestModel, files: ArrayList<MultipartBody.Part>) {
        val requestMap = HashMap<String, RequestBody>()
        requestMap["localname_en"] = fields.localNameEn.toRequestBody()
        requestMap["localname_kn"] = fields.localNameHi.toRequestBody()
        requestMap["categoryId"] = fields.categoryId.toString().toRequestBody()
        requestMap["subcategoryId"] = fields.subCategoryId.toString().toRequestBody()
        requestMap["material_id"] = fields.materialId.toString().toRequestBody()
        requestMap["template_id"] = fields.templateId.toString().toRequestBody()
        requestMap["des_en"] = fields.descriptionEn.toRequestBody()
        requestMap["des_kn"] = fields.descriptionHi.toRequestBody()
        requestMap["qty"] = fields.quantity.toString().toRequestBody()
        requestMap["price"] = fields.price.toString().toRequestBody()

        fields.video?.run { requestMap["video_url"] = toRequestBody() }
        fields.image1?.run { requestMap["image_1"] = toRequestBody() }
        fields.image2?.run { requestMap["image_2"] = toRequestBody() }
        fields.image3?.run { requestMap["image_3"] = toRequestBody() }
        fields.image4?.run { requestMap["image_4"] = toRequestBody() }
        fields.image5?.run { requestMap["image_5"] = toRequestBody() }

        fields.certificateImage1?.run { requestMap["certificate_image_1"] = toRequestBody() }
        fields.certificateImage2?.run { requestMap["certificate_image_2"] = toRequestBody() }
        fields.certificateImage3?.run { requestMap["certificate_image_3"] = toRequestBody() }
        fields.certificateImage4?.run { requestMap["certificate_image_4"] = toRequestBody() }
        fields.certificateImage5?.run { requestMap["certificate_image_5"] = toRequestBody() }
        fields.certificateImage6?.run { requestMap["certificate_image_6"] = toRequestBody() }
        fields.certificateImage7?.run { requestMap["certificate_image_7"] = toRequestBody() }

        fields.certificateTypeId1?.run { requestMap["certificate_type_1"] = toString().toRequestBody() }
        fields.certificateTypeId2?.run { requestMap["certificate_type_2"] = toString().toRequestBody() }
        fields.certificateTypeId3?.run { requestMap["certificate_type_3"] = toString().toRequestBody() }
        fields.certificateTypeId4?.run { requestMap["certificate_type_4"] = toString().toRequestBody() }
        fields.certificateTypeId5?.run { requestMap["certificate_type_5"] = toString().toRequestBody() }
        fields.certificateTypeId6?.run { requestMap["certificate_type_6"] = toString().toRequestBody() }
        fields.certificateTypeId7?.run { requestMap["certificate_type_7"] = toString().toRequestBody() }

        fields.width?.run { requestMap["width"] = toRequestBody() }
        fields.height?.run { requestMap["height"] = toRequestBody() }
        fields.length?.run { requestMap["length"] = toRequestBody() }
        fields.volume?.run { requestMap["vol"] = toRequestBody() }
        fields.weight?.run { requestMap["weight"] = toRequestBody() }

        fields.widthUnit?.run { requestMap["width_unit"] = toRequestBody() }
        fields.heightUnit?.run { requestMap["height_unit"] = toRequestBody() }
        fields.lengthUnit?.run { requestMap["length_unit"] = toRequestBody() }
        fields.volumeUnit?.run { requestMap["vol_unit"] = toRequestBody() }
        fields.weightUnit?.run { requestMap["weight_unit"] = toRequestBody() }

        fields.priceAndQuantityUnit?.run { requestMap["price_unit"] = toRequestBody() }

        requestMap["is_draft"] = fields.isDraft.toString().toRequestBody()

        requestData(
            { apiService.addProduct(requestMap, files) },
            { it.message?.run { _responseLiveData.postValue(this) }},
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun updateProduct(fields: AddProductRequestModel, files: ArrayList<MultipartBody.Part>) {
        val requestMap = HashMap<String, RequestBody>()
        requestMap["localname_en"] = fields.localNameEn.toRequestBody()
        requestMap["localname_kn"] = fields.localNameHi.toRequestBody()
        requestMap["categoryId"] = fields.categoryId.toString().toRequestBody()
        requestMap["subcategoryId"] = fields.subCategoryId.toString().toRequestBody()
        requestMap["material_id"] = fields.materialId.toString().toRequestBody()
        requestMap["template_id"] = fields.templateId.toString().toRequestBody()
        requestMap["des_en"] = fields.descriptionEn.toRequestBody()
        requestMap["des_kn"] = fields.descriptionHi.toRequestBody()
        requestMap["qty"] = fields.quantity.toString().toRequestBody()
        requestMap["price"] = fields.price.toString().toRequestBody()

        fields.video?.run { requestMap["video_url"] = toRequestBody() }
        fields.image1?.run { requestMap["image_1"] = toRequestBody() }
        fields.image2?.run { requestMap["image_2"] = toRequestBody() }
        fields.image3?.run { requestMap["image_3"] = toRequestBody() }
        fields.image4?.run { requestMap["image_4"] = toRequestBody() }
        fields.image5?.run { requestMap["image_5"] = toRequestBody() }

        fields.certificateImage1?.run { requestMap["certificate_image_1"] = toRequestBody() }
        fields.certificateImage2?.run { requestMap["certificate_image_2"] = toRequestBody() }
        fields.certificateImage3?.run { requestMap["certificate_image_3"] = toRequestBody() }
        fields.certificateImage4?.run { requestMap["certificate_image_4"] = toRequestBody() }
        fields.certificateImage5?.run { requestMap["certificate_image_5"] = toRequestBody() }
        fields.certificateImage6?.run { requestMap["certificate_image_6"] = toRequestBody() }
        fields.certificateImage7?.run { requestMap["certificate_image_7"] = toRequestBody() }

        fields.certificateTypeId1?.run { requestMap["certificate_type_1"] = toString().toRequestBody() }
        fields.certificateTypeId2?.run { requestMap["certificate_type_2"] = toString().toRequestBody() }
        fields.certificateTypeId3?.run { requestMap["certificate_type_3"] = toString().toRequestBody() }
        fields.certificateTypeId4?.run { requestMap["certificate_type_4"] = toString().toRequestBody() }
        fields.certificateTypeId5?.run { requestMap["certificate_type_5"] = toString().toRequestBody() }
        fields.certificateTypeId6?.run { requestMap["certificate_type_6"] = toString().toRequestBody() }
        fields.certificateTypeId7?.run { requestMap["certificate_type_7"] = toString().toRequestBody() }

        requestMap["is_draft"] = fields.isDraft.toString().toRequestBody()
        requestMap["productId"] = fields.productId.toString().toRequestBody()
        requestMap["is_active"] = fields.isActive.toString().toRequestBody()

        fields.width?.run { requestMap["width"] = toRequestBody() }
        fields.height?.run { requestMap["height"] = toRequestBody() }
        fields.length?.run { requestMap["length"] = toRequestBody() }
        fields.volume?.run { requestMap["vol"] = toRequestBody() }
        fields.weight?.run { requestMap["weight"] = toRequestBody() }

        fields.widthUnit?.run { requestMap["width_unit"] = toRequestBody() }
        fields.heightUnit?.run { requestMap["height_unit"] = toRequestBody() }
        fields.lengthUnit?.run { requestMap["length_unit"] = toRequestBody() }
        fields.volumeUnit?.run { requestMap["vol_unit"] = toRequestBody() }
        fields.weightUnit?.run { requestMap["weight_unit"] = toRequestBody() }

        fields.priceAndQuantityUnit?.run { requestMap["price_unit"] = toRequestBody() }

        requestData(
            { apiService.updateDraft(requestMap, files) },
            { it.message?.run { _responseLiveData.postValue(this) }},
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}