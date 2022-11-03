package com.aajeevika.clf.view.product

import android.content.Intent
import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityDraftBinding
import com.aajeevika.clf.model.data_model.AddProductRequestModel
import com.aajeevika.clf.model.data_model.ProductDraftResponse
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.product.viewmodel.DraftViewModel

class ActivityDraft : BaseActivityVM<ActivityDraftBinding, DraftViewModel>(
    R.layout.activity_draft,
    DraftViewModel::class
) {
    private lateinit var productDraftResponse: ProductDraftResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDraftDetails()
    }

    override fun observeData() {
        super.observeData()

        viewModel.draftLiveData.observe(this, {
            productDraftResponse = it

            viewDataBinding.run {
                isDraftFound = true

                productImage = BaseUrls.baseUrl + it.draftproduct.image_1
                price = it.draftproduct.price
                title = it.draftproduct.localname_en
            }
        })

        viewModel.statusLiveData.observe(this, {
            AlertDialog(
                context = this@ActivityDraft,
                cancelOnOutsideClick = false,
                message = getString(R.string.draft_cleared_successfully),
                positive = getString(R.string.ok),
                positiveClick = { finish() }
            ).show()
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            editBtn.setOnClickListener {
                if(::productDraftResponse.isInitialized) {
                    val addProductRequestModel = AddProductRequestModel().apply {
                        productDraftResponse.draftproduct.let {
                            productId = it.id.toInt()
                            categoryId = it.categoryId.toInt()
                            subCategoryId = it.subcategoryId.toInt()
                            materialId = it.material_id.toInt()
                            templateId = it.template_id.toInt()
                            localNameEn = it.localname_en
                            localNameHi = it.localname_kn
                            descriptionEn = it.des_en
                            descriptionHi = it.des_kn
                            quantity = it.qty.toInt()
                            price = it.price.toInt()
                            video = it.video_url
                            image1 = it.image_1
                            image2 = it.image_2
                            image3 = it.image_3
                            image4 = it.image_4
                            image5 = it.image_5
                        }
                    }

                    val intent = Intent(this@ActivityDraft, ActivityAddProductStepOne::class.java)
                    intent.putExtra(Constant.PRODUCT_DATA, addProductRequestModel)
                    startActivity(intent)
                }
            }

            deleteBtn.setOnClickListener {
                AlertDialog(
                    context = this@ActivityDraft,
                    message = getString(R.string.sure_clear_draft),
                    positive = getString(R.string.cancel),
                    negative = getString(R.string.delete),
                    negativeClick = {
                        if(::productDraftResponse.isInitialized)
                            viewModel.removeDraft(productDraftResponse.draftproduct.id.toInt())
                    }
                ).show()
            }
        }
    }
}