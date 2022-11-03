package com.aajeevika.clf.model.data_model

data class CertificateTypeDataModel(
    val type_list: ArrayList<CertificateData>
)

data class CertificateData(
    val id:Int,
    val name: String,
)