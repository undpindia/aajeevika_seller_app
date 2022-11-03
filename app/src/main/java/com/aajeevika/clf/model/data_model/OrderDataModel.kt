package com.aajeevika.clf.model.data_model

data class MyOrdersListData(
    val order_list: ArrayList<MyOrderData>
)

data class MyOrderData(
    val id: Int,
    val order_id_d: String,
    val seller_id: Int,
    val user_id: Int,
    val message: String,
    val interest_Id: String,
    val items: ArrayList<MyOrderItemData>,
    val seller: MyOrderSellerData,
    val buyer: MyOrderBuyerData,
)

data class MyOrderItemData(
    val id: Int,
    val product_id: Int,
    val created_at: String,
    val updated_at: String,
    val product: MyOrderProductData,
)

data class MyOrderProductData(
    val id: Int,
    val image_1: String,
    val price_unit: String?,
    val localname_en: String,
)

data class MyOrderSellerData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
)

data class MyOrderBuyerData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
)

data class OrderDetailsListData(
    val all_order: ArrayList<OrderDetailsData>,
)

data class OrderDetailsData(
    val id: Int,
    val order_id_d: String?,
    val created_at: String,
    val items: ArrayList<OrderDetailsItemsListData>,
    val interest: OrderDetailsInterestData,
    val buyer: OrderDetailsBuyerData,
    val seller: OrderDetailsSellerData,
    val seller_rating: OrderDetailsRatingData?,
    val buyer_rating: OrderDetailsRatingData?,
)

data class OrderDetailsItemsListData(
    val id: Int,
    val quantity: Int,
    val product_price: Int,
    val product: OrderDetailsProductData,
)

data class OrderDetailsProductData(
    val id: Int,
    val price: String,
    val price_unit: String?,
    val name: String,
)

data class OrderDetailsInterestData(
    val id: Int,
    val interest_Id: String?,
)

data class OrderDetailsBuyerData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val profileImage: String?,
)

data class OrderDetailsSellerData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val profileImage: String?,
    val organization_name: String?,
)

data class OrderDetailsRatingData(
    val id: Int,
    val rating: Float?,
    val review_msg: String?,
)

data class AddNewSaleResponse(
    val id: Int
)