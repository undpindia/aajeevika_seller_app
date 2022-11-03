package com.aajeevika.clf.model.data_model

data class InterestListDataModel(
    val interest_list: ArrayList<InterestData>,
)

data class InterestData(
    val id: Int,
    val interest_Id: String,
    val items: ArrayList<InterestItemBasic>,
    val buyer: InterestBuyer,
)

data class InterestItemBasic(
    val id: Int,
    val created_at: String,
    val product: ProductDataBasic,
)

data class ProductDataBasic(
    val id: Int,
    val image_1: String,
)