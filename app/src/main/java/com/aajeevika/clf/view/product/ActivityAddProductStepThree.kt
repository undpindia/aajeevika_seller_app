package com.aajeevika.clf.view.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityAddProductStepThreeBinding
import com.aajeevika.clf.model.data_model.AddProductRequestModel
import com.aajeevika.clf.utility.ApplicationData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.home.ActivityDashboard
import com.aajeevika.clf.view.product.viewmodel.ProductAddViewModel

class ActivityAddProductStepThree : BaseActivityVM<ActivityAddProductStepThreeBinding, ProductAddViewModel>(
    R.layout.activity_add_product_step_three,
    ProductAddViewModel::class
) {
    private val productRequestModel by lazy { intent.getSerializableExtra(Constant.PRODUCT_DATA) as? AddProductRequestModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.run {
            inputDescriptionEnglish.setText(ApplicationData.newProduct.descriptionEn)
            inputDescriptionHindi.setText(ApplicationData.newProduct.descriptionHi)
        }

        productRequestModel?.let {
            if(it.productId != -1) viewDataBinding.saveDraftBtn.visibility = View.GONE

            if(it.descriptionEn.isNotEmpty()) viewDataBinding.inputDescriptionEnglish.setText(it.descriptionEn)
            if(it.descriptionHi.isNotEmpty()) viewDataBinding.inputDescriptionHindi.setText(it.descriptionHi)
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.responseLiveData.observe(this, {
            AlertDialog(
                context = this,
                cancelOnOutsideClick = false,
                message = it,
                positive = getString(R.string.ok),
                positiveClick = {
                    val intent = Intent(this, ActivityDashboard::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            ).show()
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            toolbar.editBtn.setOnClickListener {
                viewDataBinding.editable = !viewDataBinding.editable
                viewDataBinding.executePendingBindings()

                if(viewDataBinding.editable) {
                    inputDescriptionEnglish.background = ContextCompat.getDrawable(this@ActivityAddProductStepThree, R.drawable.outline_w1_gray_100_r4)
                    inputDescriptionHindi.background = ContextCompat.getDrawable(this@ActivityAddProductStepThree, R.drawable.outline_w1_gray_100_r4)

                    inputDescriptionEnglish.setPadding(UtilityActions.pxFromDp(this@ActivityAddProductStepThree, 8F).toInt())
                    inputDescriptionHindi.setPadding(UtilityActions.pxFromDp(this@ActivityAddProductStepThree, 8F).toInt())
                } else {
                    inputDescriptionEnglish.background = null
                    inputDescriptionHindi.background = null

                    inputDescriptionEnglish.setPadding(0)
                    inputDescriptionHindi.setPadding(0)
                }
            }

            saveDraftBtn.setOnClickListener {
                val descriptionEnglish = inputDescriptionEnglish.text.toString().trim()
                val descriptionHindi = inputDescriptionHindi.text.toString().trim()

                validateFormData(descriptionEnglish, descriptionHindi)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    ApplicationData.newProduct.descriptionEn = descriptionEnglish
                    ApplicationData.newProduct.descriptionHi = descriptionHindi

                    ApplicationData.newProduct.isDraft = 1

                    viewModel.addProduct(ApplicationData.newProduct, ApplicationData.files)
                }
            }

            addBtn.setOnClickListener {
                val descriptionEnglish = inputDescriptionEnglish.text.toString().trim()
                val descriptionHindi = inputDescriptionHindi.text.toString().trim()

                validateFormData(descriptionEnglish, descriptionHindi)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    ApplicationData.newProduct.descriptionEn = descriptionEnglish
                    ApplicationData.newProduct.descriptionHi = descriptionHindi

                    ApplicationData.newProduct.isDraft = 0

                    productRequestModel?.productId?.let {
                        if(it != -1) {
                            ApplicationData.newProduct.productId = it
                            ApplicationData.newProduct.isActive = 1

                            viewModel.updateProduct(ApplicationData.newProduct, ApplicationData.files)
                        } else {
                            viewModel.addProduct(ApplicationData.newProduct, ApplicationData.files)
                        }
                    }
                }
            }
        }
    }

    private fun validateFormData(descriptionEnglish: String, descriptionHindi: String) : String? {
        return if(descriptionEnglish.isEmpty() || descriptionHindi.isEmpty()) getString(R.string.fill_all_the_fields)
        else null
    }
}