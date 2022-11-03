package com.aajeevika.clf.model.data_model

data class CollectionCenterDataModel(
    val collectionCenter: ArrayList<CollectionCenterData>
)

data class CollectionCenterData(
    val id: Int,
    val name: String,
)
