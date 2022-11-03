package com.aajeevika.clf.model.data_model

data class ProductCategoryResponse(
    var category: ArrayList<ProductCategoryData>,
)

data class ProductCategoryData(
    var id: Int,
    var name: String,
    var image: String,
)

data class MaterialResponse(
    var material: ArrayList<MaterialData>,
)

data class MaterialData(
    var id: Int,
    var name: String,
)

data class TemplateResponse(
    var template: ArrayList<TemplateData>
)

data class TemplateData(
    var id: Int,
    var name: String,
    var description_en: String,
    var description_kn: String,
    var no_measurement: String?,
    var volume: String?,
    var weight: String?,
    var height: String?,
    var length: String?,
    var width: String?,
)