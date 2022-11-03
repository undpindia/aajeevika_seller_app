package com.aajeevika.clf.model.data_model

data class BuyManagerDataModel(
    val order_list: ArrayList<BuyManagerOrderData>
)

data class BuyManagerOrderData(
    val id: Int,
    val order_id_d: String,
    val created_at: String,
    val get_individual: BuyManagerSellerData,
)

data class BuyManagerSellerData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
)