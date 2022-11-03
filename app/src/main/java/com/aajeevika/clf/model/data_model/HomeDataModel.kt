package com.aajeevika.clf.model.data_model

data class HomeDataModel(
    val categories: HomePageData,
    val products: ArrayList<CategoryData>,
)

data class HomePageData(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
)

data class CategoryData(
    val categoryId: Int,
    val categoryName: String,
    val subCategories: ArrayList<SubCategoryData>,
)

data class SubCategoryData(
    val subCategoryId: Int,
    val subCategoryName: String,
    val products: ArrayList<ProductData>
)

data class ProductData(
    val id: Int,
    val name: String,
    val price: String,
    val image_1: String,
    val product_id_d: String,
    val template: ProductTemplate,
)

data class ProductTemplate(
    val name: String?
)