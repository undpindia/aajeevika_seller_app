package com.aajeevika.clf.model.data_model

data class SearchDataModel(
    val products: SearchResultData,
)

data class SearchResultData(
    val last_page : Int,
    val current_page: Int,
    val data : ArrayList<SearchProductData>
)

data class SearchProductData(
    val id: Int,
    val name: String,
    val price: String,
    val image_1: String,
    val product_id_d: String,
    val template: SearchProductTemplateData,
)

data class SearchProductTemplateData(
    val name: String,
)