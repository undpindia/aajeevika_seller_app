package com.aajeevika.clf.model.data_model

data class RatingDataModel(
    val ratings: ArrayList<RatingListData>,
    val pagination: RatingPagination,
)

data class RatingListData(
    val id: Int,
    val rating: Float,
    val review_msg: String,
    val getreviews: RatingUserData,
)

data class RatingUserData(
    val id: Int,
    val name: String,
    val profileImage: String?,
)

data class RatingPagination(
    val current_page: Int,
    val last_page: Int,
)