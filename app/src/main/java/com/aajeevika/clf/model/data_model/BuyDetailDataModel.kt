package com.aajeevika.clf.model.data_model

data class BuyDetailDataModel(
    val order_details: BuyDetailOrderData,
)

data class BuyDetailOrderData(
    val id: Int,
    val order_id_d: String,
    val created_at: String,
    val ind_items: ArrayList<BuyDetailItemData>,
    val get_individual: BuyDetailBuyerData,
    val get_clf: BuyDetailBuyerData,
    val clf_rating: OrderRatingData?,
    val ind_rating: OrderRatingData?,
)

data class BuyDetailItemData(
    val id: Int,
    val quantity: Int,
    val indproduct: BuyDetailProductData,
)

data class BuyDetailProductData(
    val id: Int,
    val name_en: String,
    val price_unit: String,
)

data class BuyDetailBuyerData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val profileImage: String?,
)

data class OrderRatingData(
    val id: Int,
    val rating: Float,
    val review_msg: String,
)