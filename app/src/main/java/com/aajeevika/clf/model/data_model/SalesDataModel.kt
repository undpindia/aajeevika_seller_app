package com.aajeevika.clf.model.data_model

import java.io.Serializable

data class SalesListData(
    val order_list: ArrayList<SalesData>
)

data class SalesData(
    val id: Int,
    val order_id_d: String,
    val seller_id: Int,
    val user_id: Int,
    val message: String,
    val interest_Id: String,
    val items: ArrayList<SalesItemsData>,
    val seller: SalesSellerData,
    val buyer: SalesBuyerData,
)

data class SalesItemsData(
    val id: Int,
    val product_id: Int,
    val created_at: String,
    val updated_at: String,
    val product: SalesProductData,
)

data class SalesProductData(
    val id: Int,
    val image_1: String,
    val price_unit: String?,
    val localname_en: String,
)

data class SalesBuyerData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
)

data class SalesSellerData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
)

data class SalesSellerProductsDataModel(
    val seller_data: ArrayList<SalesSellerProducts>
) : Serializable

data class SalesSellerProducts(
    val id: Int,
    val qty: Int,
    val name: String?,
    val price: String,
    val price_unit: String?,
    val image_1: String?,
    var quantity: Int = 0,
) : Serializable