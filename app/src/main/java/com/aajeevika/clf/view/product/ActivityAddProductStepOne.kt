package com.aajeevika.clf.view.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityAddProductStepOneBinding
import com.aajeevika.clf.model.data_model.AddProductRequestModel
import com.aajeevika.clf.model.data_model.TemplateData
import com.aajeevika.clf.utility.ApplicationData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.view.product.viewmodel.ProductAddViewModel

class ActivityAddProductStepOne : BaseActivityVM<ActivityAddProductStepOneBinding, ProductAddViewModel>(
    R.layout.activity_add_product_step_one,
    ProductAddViewModel::class
) {
    private var categoryId: Int = -1
    private var materialId: Int = -1
    private var subCategoryId: Int = -1

    private val productRequestModel by lazy {
        (intent.getSerializableExtra(Constant.PRODUCT_DATA) as? AddProductRequestModel) ?: AddProductRequestModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationData.newProduct = AddProductRequestModel()
        ApplicationData.files = ArrayList()

        viewModel.getCategoryList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.categoryLiveData.observe(this, { list ->
            viewDataBinding.categoryList = list.category.map { it.name }.toCollection(ArrayList<String>())

            if (productRequestModel.categoryId != -1) {
                list.category.firstOrNull { it.id == productRequestModel.categoryId }?.name?.run {
                    viewDataBinding.inputSelectCategory.setText(this)
                }
            }
        })

        viewModel.subCategoryLiveData.observe(this, { list ->
            viewDataBinding.subCategoryList = list.category.map { it.name }.toCollection(ArrayList<String>())

            if (productRequestModel.subCategoryId != -1) {
                list.category.firstOrNull { it.id == productRequestModel.subCategoryId }?.name?.run {
                    viewDataBinding.inputSelectSubCategory.setText(this)
                }
            }
        })

        viewModel.materialLiveData.observe(this, { list ->
            viewDataBinding.materialList = list.material.map { it.name }.toCollection(ArrayList<String>())

            if (productRequestModel.materialId != -1) {
                list.material.firstOrNull { it.id == productRequestModel.materialId }?.name?.run {
                    viewDataBinding.inputProductType.setText(this)
                }
            }
        })

        viewModel.templateLiveData.observe(this, { list ->
            viewDataBinding.productList = list.template.map { it.name }.toCollection(ArrayList<String>())

            if (productRequestModel.templateId != -1) {
                list.template.firstOrNull { it.id == productRequestModel.templateId }?.run {
                    productRequestModel.isWidthIsOn = width?.equals("ON", true) == true
                    productRequestModel.isHeightIsOn = height?.equals("ON", true) == true
                    productRequestModel.isLengthIsOn = length?.equals("ON", true) == true
                    productRequestModel.isVolumeIsOn = volume?.equals("ON", true) == true
                    productRequestModel.isWeightIsOn = weight?.equals("ON", true) == true

                    viewDataBinding.inputProductName.setText(name)
                    prePopulatedData(productRequestModel)
                }
            }

            viewDataBinding.inputLocalNameHindi.setText(productRequestModel.localNameHi)
            viewDataBinding.inputLocalNameEnglish.setText(productRequestModel.localNameEn)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            nextBtn.setOnClickListener {
                val categoryName = inputSelectCategory.text.toString().trim()
                val subCategoryName = inputSelectSubCategory.text.toString().trim()
                val productType = inputProductType.text.toString().trim()
                val productName = inputProductName.text.toString().trim()
                val localNameEnglish = inputLocalNameEnglish.text.toString().trim()
                val localNameHindi = inputLocalNameHindi.text.toString().trim()

                validateFormData(categoryName, subCategoryName, productType, productName, localNameEnglish, localNameHindi)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    ApplicationData.newProduct.localNameEn = localNameEnglish
                    ApplicationData.newProduct.localNameHi = localNameHindi

                    viewModel.categoryLiveData.value?.category?.firstOrNull { it.name == categoryName }?.let { data ->
                        ApplicationData.newProduct.categoryId = data.id
                    }

                    viewModel.subCategoryLiveData.value?.category?.firstOrNull { it.name == subCategoryName }?.let { data ->
                        ApplicationData.newProduct.subCategoryId = data.id
                    }

                    viewModel.materialLiveData.value?.material?.firstOrNull { it.name == productType }?.let { data ->
                        ApplicationData.newProduct.materialId = data.id
                    }

                    viewModel.templateLiveData.value?.template?.firstOrNull { it.name == productName }?.let { data ->
                        ApplicationData.newProduct.templateId = data.id
                        ApplicationData.newProduct.descriptionEn = data.description_en
                        ApplicationData.newProduct.descriptionHi = data.description_kn
                    }

                    ApplicationData.newProduct.run {
                        if(categoryId > 0 && subCategoryId > 0 && materialId > 0 && templateId > 0) {
                            if (widthContainer.isVisible) {
                                ApplicationData.newProduct.width = inputWidthDimension.text.toString().trim()
                                ApplicationData.newProduct.widthUnit = widthDimensionUnit.text.toString().trim()
                            }

                            if (heightContainer.isVisible) {
                                ApplicationData.newProduct.height = inputHeightDimension.text.toString().trim()
                                ApplicationData.newProduct.heightUnit = heightDimensionUnit.text.toString().trim()
                            }

                            if (lengthContainer.isVisible) {
                                ApplicationData.newProduct.length = inputLengthDimension.text.toString().trim()
                                ApplicationData.newProduct.lengthUnit = lengthDimensionUnit.text.toString().trim()
                            }

                            if (volumeContainer.isVisible) {
                                ApplicationData.newProduct.volume = inputVolumeDimension.text.toString().trim()
                                ApplicationData.newProduct.volumeUnit = volumeDimensionUnit.text.toString().trim()
                            }

                            if (weightContainer.isVisible) {
                                ApplicationData.newProduct.weight = inputWeightDimension.text.toString().trim()
                                ApplicationData.newProduct.weightUnit = weightDimensionUnit.text.toString().trim()
                            }

                            val intent = Intent(this@ActivityAddProductStepOne, ActivityAddProductStepTwo::class.java)
                            intent.putExtra(Constant.PRODUCT_DATA, productRequestModel)
                            startActivity(intent)
                        } else {
                            root.showMessage(getString(R.string.fill_all_the_fields))
                        }
                    }
                }
            }

            inputSelectCategory.doOnTextChanged { text, _, _, _ ->
                text?.toString()?.let { category ->
                    viewModel.categoryLiveData.value?.category?.firstOrNull { it.name == category }?.let { data ->
                        inputSelectSubCategory.text = null
                        subCategoryId = -1

                        categoryId = data.id
                        viewModel.getSubCategoryList(data.id)
                    }
                }
            }

            inputSelectSubCategory.doOnTextChanged { text, _, _, _ ->
                text?.toString()?.let { subCategory ->
                    viewModel.subCategoryLiveData.value?.category?.firstOrNull { it.name == subCategory }?.let { data ->
                        inputProductType.text = null
                        materialId = -1

                        subCategoryId = data.id
                        viewModel.getMaterials(data.id)
                    }
                }
            }

            inputProductType.doOnTextChanged { text, _, _, _ ->
                text?.toString()?.let { subCategory ->
                    viewModel.materialLiveData.value?.material?.firstOrNull { it.name == subCategory }?.let { data ->
                        inputProductName.text = null
                        productList = null

                        materialId = data.id
                        viewModel.getTemplate(categoryId, subCategoryId, materialId)
                    }
                }
            }

            inputProductName.doOnTextChanged { text, _, _, _ ->
                text?.toString()?.let { productName ->
                    viewModel.templateLiveData.value?.template?.firstOrNull { it.name == productName }?.let { data ->
                        handleDimensionsInputFields(data)
                    }
                }
            }
        }
    }

    private fun validateFormData(category: String, subCategory: String, productType: String, productName: String, localNameEnglish: String, localNameHindi: String): String? {
        return if (category.isEmpty() || subCategory.isEmpty() || productType.isEmpty() || productName.isEmpty() || localNameEnglish.isEmpty() || localNameHindi.isEmpty())
            getString(R.string.fill_all_the_fields)
        else if (viewDataBinding.widthContainer.isVisible && viewDataBinding.inputWidthDimension.text.toString().isEmpty())
            getString(R.string.fill_all_the_fields)
        else if (viewDataBinding.heightContainer.isVisible && viewDataBinding.inputHeightDimension.text.toString().isEmpty())
            getString(R.string.fill_all_the_fields)
        else if (viewDataBinding.lengthContainer.isVisible && viewDataBinding.inputLengthDimension.text.toString().isEmpty())
            getString(R.string.fill_all_the_fields)
        else if (viewDataBinding.volumeContainer.isVisible && viewDataBinding.inputVolumeDimension.text.toString().isEmpty())
            getString(R.string.fill_all_the_fields)
        else if (viewDataBinding.weightContainer.isVisible && viewDataBinding.inputWeightDimension.text.toString().isEmpty())
            getString(R.string.fill_all_the_fields)
        else null
    }

    private fun handleDimensionsInputFields(data: TemplateData) {
        viewDataBinding.run {
            inputWidthDimension.clearFocus()
            inputHeightDimension.clearFocus()
            inputLengthDimension.clearFocus()
            inputWeightDimension.clearFocus()
            inputVolumeDimension.clearFocus()

            inputWidthDimension.text = null
            inputHeightDimension.text = null
            inputLengthDimension.text = null
            inputWeightDimension.text = null
            inputVolumeDimension.text = null

            widthDimensionUnit.setText(getString(R.string.default_size_unit))
            heightDimensionUnit.setText(getString(R.string.default_size_unit))
            lengthDimensionUnit.setText(getString(R.string.default_size_unit))
            weightDimensionUnit.setText(getString(R.string.default_weight_unit))
            volumeDimensionUnit.setText(getString(R.string.default_volume_unit))

            sizeUnitList = resources.getStringArray(R.array.size_units).toCollection(ArrayList<String>())
            volumeUnitList = resources.getStringArray(R.array.volume_units).toCollection(ArrayList<String>())
            weightUnitList = resources.getStringArray(R.array.weight_list).toCollection(ArrayList<String>())

            widthContainer.visibility = if(data.width?.equals("ON", true) == true) View.VISIBLE else View.GONE
            heightContainer.visibility = if(data.height?.equals("ON", true) == true) View.VISIBLE else View.GONE
            lengthContainer.visibility = if(data.length?.equals("ON", true) == true) View.VISIBLE else View.GONE
            weightContainer.visibility = if(data.weight?.equals("ON", true) == true) View.VISIBLE else View.GONE
            volumeContainer.visibility = if(data.volume?.equals("ON", true) == true) View.VISIBLE else View.GONE

            if(widthContainer.isVisible || heightContainer.isVisible || lengthContainer.isVisible || weightContainer.isVisible || volumeContainer.isVisible) {
                productDimensionTxt.visibility = View.VISIBLE
            } else {
                productDimensionTxt.visibility = View.GONE
            }
        }
    }

    private fun prePopulatedData(addProductRequestModel: AddProductRequestModel) {
        viewDataBinding.run {
            inputWidthDimension.setText(addProductRequestModel.width)
            inputHeightDimension.setText(addProductRequestModel.height)
            inputLengthDimension.setText(addProductRequestModel.length)
            inputWeightDimension.setText(addProductRequestModel.weight)
            inputVolumeDimension.setText(addProductRequestModel.volume)

            widthDimensionUnit.setText(addProductRequestModel.widthUnit ?: getString(R.string.default_size_unit))
            heightDimensionUnit.setText(addProductRequestModel.heightUnit ?: getString(R.string.default_size_unit))
            lengthDimensionUnit.setText(addProductRequestModel.lengthUnit ?: getString(R.string.default_size_unit))
            weightDimensionUnit.setText(addProductRequestModel.weightUnit ?: getString(R.string.default_weight_unit))
            volumeDimensionUnit.setText(addProductRequestModel.volumeUnit ?: getString(R.string.default_volume_unit))

            sizeUnitList = resources.getStringArray(R.array.size_units).toCollection(ArrayList<String>())
            volumeUnitList = resources.getStringArray(R.array.volume_units).toCollection(ArrayList<String>())
            weightUnitList = resources.getStringArray(R.array.weight_list).toCollection(ArrayList<String>())

            widthContainer.visibility = if(addProductRequestModel.isWidthIsOn) View.VISIBLE else View.GONE
            heightContainer.visibility = if(addProductRequestModel.isHeightIsOn) View.VISIBLE else View.GONE
            lengthContainer.visibility = if(addProductRequestModel.isLengthIsOn) View.VISIBLE else View.GONE
            weightContainer.visibility = if(addProductRequestModel.isWeightIsOn) View.VISIBLE else View.GONE
            volumeContainer.visibility = if(addProductRequestModel.isVolumeIsOn) View.VISIBLE else View.GONE

            if(widthContainer.isVisible || heightContainer.isVisible || lengthContainer.isVisible || weightContainer.isVisible || volumeContainer.isVisible) {
                productDimensionTxt.visibility = View.VISIBLE
            } else {
                productDimensionTxt.visibility = View.GONE
            }
        }
    }
}