package com.aajeevika.clf.model.data_model

import java.io.Serializable

data class ProductDraftResponse(
    var draftproduct: DraftInfo
)

data class DraftInfo(
    var id: String,
    var material_id: String,
    var categoryId: String,
    var subcategoryId: String,
    var price: String,
    var qty: String,
    var localname_en: String,
    var localname_kn: String,
    var length: String?,
    var width: String?,
    var height: String?,
    var weight: String?,
    var vol: String?,
    var length_unit: String?,
    var width_unit: String?,
    var height_unit: String?,
    var weight_unit: String?,
    var vol_unit: String?,
    var image_1: String?,
    var image_2: String?,
    var image_3: String?,
    var image_4: String?,
    var image_5: String?,
    var video_url: String?,
    var des_en: String,
    var des_kn: String,
    var template_id: String,
): Serializable