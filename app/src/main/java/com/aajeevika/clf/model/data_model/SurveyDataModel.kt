package com.aajeevika.clf.model.data_model

data class SurveyDataModel(
    val survey_list: ArrayList<SurveyData>
)

data class SurveyData(
    val id: Int,
    val message: String,
    val google_url: String,
    val start_date: String,
    val end_date: String,
)
