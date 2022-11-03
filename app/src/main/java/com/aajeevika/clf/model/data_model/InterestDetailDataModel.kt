package com.aajeevika.clf.model.data_model

data class InterestDetailDataModel(
    val seller_interest: ArrayList<Interest>
)

data class Interest(
    val id: Int,
    val message: String,
    val interest_Id: String,
    val created_at: String,
    val buyer: InterestBuyer,
    val items: ArrayList<InterestItems>,
)

data class InterestItems(
    val id: Int,
    val quantity: Int,
    val product: InterestProductData,
)

data class InterestProductData(
    val id: Int,
    val price: Int,
    val image_1: String?,
    val price_unit: String?,
    val name: String?,
)

data class InterestBuyer(
    val id: Int,
    val name: String,
    val mobile: String,
    val email: String,
    val profileImage: String?,
)