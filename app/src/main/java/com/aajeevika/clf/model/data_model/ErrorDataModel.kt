package com.aajeevika.clf.model.data_model

import com.aajeevika.clf.utility.app_enum.ErrorType

data class ErrorDataModel(
    val message: String,
    val errorType: ErrorType,
)