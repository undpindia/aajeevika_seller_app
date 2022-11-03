package com.aajeevika.clf.model.data_model

import java.io.Serializable

class AddProductRequestModel : Serializable {
    var productId: Int = -1
    var categoryId: Int = -1
    var subCategoryId: Int = -1
    var materialId: Int = -1
    var templateId: Int = -1
    var localNameEn: String = ""
    var localNameHi: String = ""
    var descriptionEn: String = ""
    var descriptionHi: String = ""
    var quantity: Int = -1
    var price: Int = -1
    var isDraft: Int = 0
    var isActive: Int = -1
    var video: String? = null
    var image1: String? = null
    var image2: String? = null
    var image3: String? = null
    var image4: String? = null
    var image5: String? = null

    var width: String? = null
    var widthUnit: String? = null
    var height: String? = null
    var heightUnit: String? = null
    var length: String? = null
    var lengthUnit: String? = null
    var volume: String? = null
    var volumeUnit: String? = null
    var weight: String? = null
    var weightUnit: String? = null
    var priceAndQuantityUnit: String? = null

    var isWidthIsOn: Boolean = false
    var isHeightIsOn: Boolean = false
    var isLengthIsOn: Boolean = false
    var isVolumeIsOn: Boolean = false
    var isWeightIsOn: Boolean = false

    var certificateImage1: String? = null
    var certificateImage2: String? = null
    var certificateImage3: String? = null
    var certificateImage4: String? = null
    var certificateImage5: String? = null
    var certificateImage6: String? = null
    var certificateImage7: String? = null

    var certificateTypeId1: Int? = null
    var certificateTypeId2: Int? = null
    var certificateTypeId3: Int? = null
    var certificateTypeId4: Int? = null
    var certificateTypeId5: Int? = null
    var certificateTypeId6: Int? = null
    var certificateTypeId7: Int? = null

    var certificateStatue1: Int? = null
    var certificateStatue2: Int? = null
    var certificateStatue3: Int? = null
    var certificateStatue4: Int? = null
    var certificateStatue5: Int? = null
    var certificateStatue6: Int? = null
    var certificateStatue7: Int? = null
}