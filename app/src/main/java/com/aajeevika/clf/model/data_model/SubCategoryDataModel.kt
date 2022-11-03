package com.aajeevika.clf.model.data_model

data class SubCategoryDataModel(
    val pagination: SubCategoryPaginationData,
    val products: ArrayList<SubCategoryProductData>,
)

data class SubCategoryPaginationData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
)

data class SubCategoryProductData(
    val id: Int,
    val name: String,
    val image_1: String,
    val price: String,
    val product_id_d: String,
    val template: SubCategoryTemplate,
)

data class SubCategoryTemplate(
    val name: String?
)