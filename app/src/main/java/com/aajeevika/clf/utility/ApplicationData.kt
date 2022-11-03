package com.aajeevika.clf.utility

import com.aajeevika.clf.model.data_model.AddProductRequestModel
import okhttp3.MultipartBody

object ApplicationData {
    lateinit var newProduct: AddProductRequestModel
    lateinit var files: ArrayList<MultipartBody.Part>
}