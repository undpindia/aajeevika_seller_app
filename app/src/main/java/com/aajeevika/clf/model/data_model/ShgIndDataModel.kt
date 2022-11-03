package com.aajeevika.clf.model.data_model

data class ShgIndDataModel(
    val individual_list: ArrayList<IndividualData>
)

data class IndividualData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val ratingAvgStar: Float?,
    val address_line_one: String?,
    val address_line_two: String?,
    val country: String?,
    val state: String?,
    val district: String?,
    val block: String?,
    val pincode: String?,
)
