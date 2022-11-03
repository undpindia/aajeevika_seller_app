package com.aajeevika.clf.view.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityProductDetailsBinding
import com.aajeevika.clf.databinding.ListItemCertificateBinding
import com.aajeevika.clf.model.data_model.AddProductRequestModel
import com.aajeevika.clf.model.data_model.ProductDetailDataModel
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.utility.app_enum.ProductMediaType
import com.aajeevika.clf.utility.loadImageFromNetwork
import com.aajeevika.clf.view.product.adapter.ProductDetailPreviewRecyclerViewAdapter
import com.aajeevika.clf.view.product.viewmodel.ProductDetailViewModel

class ActivityProductDetail : BaseActivityVM<ActivityProductDetailsBinding, ProductDetailViewModel>(
    R.layout.activity_product_details,
    ProductDetailViewModel::class
) {
    private val productDetailPreviewRecyclerViewAdapter = ProductDetailPreviewRecyclerViewAdapter()
    private val productId by lazy { intent.getIntExtra(Constant.PRODUCT_ID, -1) }

    private lateinit var productDetailDataModel: ProductDetailDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = productDetailPreviewRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(4F,4F,4F,4F))
            (layoutManager as? GridLayoutManager)?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if(position == 0) 4 else 1
                }
            }
        }

        viewModel.getProductDetails(productId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.productDetailLiveData.observe(this, {
            productDetailDataModel = it

            val productMediaList = ArrayList<Pair<ProductMediaType, String>>()
            it.productdetail.video_url?.run { productMediaList.add(Pair(ProductMediaType.VIDEO, this)) }
            it.productdetail.image_1?.run { productMediaList.add(Pair(ProductMediaType.IMAGE, this)) }
            it.productdetail.image_2?.run { productMediaList.add(Pair(ProductMediaType.IMAGE, this)) }
            it.productdetail.image_3?.run { productMediaList.add(Pair(ProductMediaType.IMAGE, this)) }
            it.productdetail.image_4?.run { productMediaList.add(Pair(ProductMediaType.IMAGE, this)) }
            it.productdetail.image_5?.run { productMediaList.add(Pair(ProductMediaType.IMAGE, this)) }
            productDetailPreviewRecyclerViewAdapter.addData(productMediaList)

            viewDataBinding.run {
                productId = it.productdetail.product_id_d
                title = "${ it.productdetail.template_name } (${ it.productdetail.name })"
                price = it.productdetail.price
                quantity = it.productdetail.qty
                description = it.productdetail.description
                priceUnit = it.productdetail.price_unit

                val subTitleStringBuilder = StringBuilder("${ it.productdetail.catName } | ${ it.productdetail.SubCatName }")
                it.productdetail.width?.run { subTitleStringBuilder.append("\n${getString(R.string.width)}: $this ${it.productdetail.width_unit}") }
                it.productdetail.height?.run { subTitleStringBuilder.append("\n${getString(R.string.height)}: $this ${it.productdetail.height_unit}") }
                it.productdetail.length?.run { subTitleStringBuilder.append("\n${getString(R.string.length)}: $this ${it.productdetail.length_unit}") }
                it.productdetail.vol?.run { subTitleStringBuilder.append("\n${getString(R.string.volume)}: $this ${it.productdetail.vol_unit}") }
                it.productdetail.weight?.run { subTitleStringBuilder.append("\n${getString(R.string.weight)}: $this ${it.productdetail.weight_unit}") }

                subTitle = subTitleStringBuilder.toString()

                executePendingBindings()
            }

            viewDataBinding.run {
                it.productdetail.certificate_data?.certificate_image_1?.run {
                    certificatesTxt.visibility = View.VISIBLE
                    certificateList.visibility = View.VISIBLE
                    divider.visibility = View.VISIBLE
                }

                certificateList.removeAllViews()
                it.productdetail.certificate_data?.let { cert ->
                    cert.certificate_image_1?.run { initCert(certificateList, this, cert.certificate_status_1, cert.certificate_type_name_1) }
                    cert.certificate_image_2?.run { initCert(certificateList, this, cert.certificate_status_2, cert.certificate_type_name_2) }
                    cert.certificate_image_3?.run { initCert(certificateList, this, cert.certificate_status_3, cert.certificate_type_name_3) }
                    cert.certificate_image_4?.run { initCert(certificateList, this, cert.certificate_status_4, cert.certificate_type_name_4) }
                    cert.certificate_image_5?.run { initCert(certificateList, this, cert.certificate_status_5, cert.certificate_type_name_5) }
                    cert.certificate_image_6?.run { initCert(certificateList, this, cert.certificate_status_6, cert.certificate_type_name_6) }
                    cert.certificate_image_7?.run { initCert(certificateList, this, cert.certificate_status_7, cert.certificate_type_name_7) }
                }
            }
        })
    }

    private fun initCert(list: LinearLayout, image: String, status: Int?, name: String?) {
        val binding = ListItemCertificateBinding.inflate(layoutInflater, list, false)
        binding.run {
            certificateName.text = name
            certificateImage.loadImageFromNetwork(BaseUrls.baseUrl + image, null)
            certificateStatus.text = when(status) {
                0 -> getString(R.string.pending)
                1 -> getString(R.string.verified)
                else -> getString(R.string.rejected)
            }
            certificateStatus.setTextColor(ContextCompat.getColor(this@ActivityProductDetail, when(status) {
                0 -> R.color.orange
                1 -> R.color.green
                else -> R.color.red
            }))
        }

        list.addView(binding.root)
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            editBtn.setOnClickListener {
                val addProductRequestModel = AddProductRequestModel().apply {
                    productDetailDataModel.productdetail.let {
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
                        width = it.width
                        height = it.height
                        length = it.length
                        volume = it.vol
                        weight = it.weight
                        widthUnit = it.width_unit
                        heightUnit = it.height_unit
                        lengthUnit = it.length_unit
                        volumeUnit = it.vol_unit
                        weightUnit = it.weight_unit
                        priceAndQuantityUnit = it.price_unit
                        certificateImage1 = it.certificate_data?.certificate_image_1
                        certificateTypeId1 = it.certificate_data?.certificate_type_1
                        certificateStatue1 = it.certificate_data?.certificate_status_1
                        certificateImage2 = it.certificate_data?.certificate_image_2
                        certificateTypeId2 = it.certificate_data?.certificate_type_2
                        certificateStatue2 = it.certificate_data?.certificate_status_2
                        certificateImage3 = it.certificate_data?.certificate_image_3
                        certificateTypeId3 = it.certificate_data?.certificate_type_3
                        certificateStatue3 = it.certificate_data?.certificate_status_3
                        certificateImage4 = it.certificate_data?.certificate_image_4
                        certificateTypeId4 = it.certificate_data?.certificate_type_4
                        certificateStatue4 = it.certificate_data?.certificate_status_4
                        certificateImage5 = it.certificate_data?.certificate_image_5
                        certificateTypeId5 = it.certificate_data?.certificate_type_5
                        certificateStatue5 = it.certificate_data?.certificate_status_5
                        certificateImage6 = it.certificate_data?.certificate_image_6
                        certificateTypeId6 = it.certificate_data?.certificate_type_6
                        certificateStatue6 = it.certificate_data?.certificate_status_6
                        certificateImage7 = it.certificate_data?.certificate_image_7
                        certificateTypeId7 = it.certificate_data?.certificate_type_7
                        certificateStatue7 = it.certificate_data?.certificate_status_7
                    }
                }

                val intent = Intent(this@ActivityProductDetail, ActivityAddProductStepOne::class.java)
                intent.putExtra(Constant.PRODUCT_DATA, addProductRequestModel)
                startActivity(intent)
            }
        }
    }
}