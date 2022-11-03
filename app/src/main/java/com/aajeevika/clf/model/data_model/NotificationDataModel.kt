package com.aajeevika.clf.model.data_model

data class NotificationDataModel(
    var pagination: NotificationPaginationData,
    var getnotification: ArrayList<NotificationData>,
)

data class NotificationPaginationData(
    var current_page: Int,
    var last_page: Int,
)

data class NotificationData(
    var id: Int,
    var title: String,
    var body: String,
    var image: String?,
    var created_at: String?,
)