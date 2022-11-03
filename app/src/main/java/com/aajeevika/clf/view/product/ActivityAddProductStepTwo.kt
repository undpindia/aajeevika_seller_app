package com.aajeevika.clf.view.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityAddProductStepTwoBinding
import com.aajeevika.clf.databinding.ListItemAddProductImageBinding
import com.aajeevika.clf.model.data_model.AddProductRequestModel
import com.aajeevika.clf.utility.*
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.utility.app_enum.ProductMediaType
import com.aajeevika.clf.view.dialog.MediaDialog
import com.aajeevika.clf.view.product.adapter.AddProductImageRecyclerViewAdapter
import com.aajeevika.clf.view.product.viewmodel.ProductAddViewModel
import com.yalantis.ucrop.UCrop
import java.io.File
import java.lang.StringBuilder

class ActivityAddProductStepTwo : BaseActivityVM<ActivityAddProductStepTwoBinding, ProductAddViewModel>(
    R.layout.activity_add_product_step_two,
    ProductAddViewModel::class
) {
    private lateinit var addProductImageRecyclerViewAdapter: AddProductImageRecyclerViewAdapter
    private val productRequestModel by lazy { intent.getSerializableExtra(Constant.PRODUCT_DATA) as? AddProductRequestModel }
    private val certificateImageMap = HashMap<Int, Pair<CertificateImageType, Any>>()
    private val selectedFileName = StringBuilder()
    private val fileRequestFrom = StringBuilder()
    private var certificateIndex = -1

    private val galleryResultCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            val file = File(cacheDir, selectedFileName.toString())
            if (!file.exists()) file.createNewFile()

            UtilityActions.startUcropActivityForResult(this, it, file.toUri(), uCropCallBack, 1F, 1F)
        }
    }

    private val cameraResultCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it == true) {
            val file = File(cacheDir, selectedFileName.toString())

            UtilityActions.startUcropActivityForResult(this, file.toUri(), file.toUri(), uCropCallBack, 1F, 1F)
        }
    }

    private val uCropCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.data?.let { intent ->
            UCrop.getOutput(intent)?.toFile()?.let { file ->
                when(fileRequestFrom.toString()) {
                    AddProductImageRecyclerViewAdapter::class.simpleName -> addProductImageRecyclerViewAdapter.addImage(file)
                    ActivityAddProductStepTwo::class.simpleName -> {
                        certificateImageMap[certificateIndex] = Pair(CertificateImageType.FILE, file)

                        when(certificateIndex) {
                            1 -> viewDataBinding.certificateImage1.setImage(file.toUri())
                            2 -> viewDataBinding.certificateImage2.setImage(file.toUri())
                            3 -> viewDataBinding.certificateImage3.setImage(file.toUri())
                            4 -> viewDataBinding.certificateImage4.setImage(file.toUri())
                            5 -> viewDataBinding.certificateImage5.setImage(file.toUri())
                            6 -> viewDataBinding.certificateImage6.setImage(file.toUri())
                            7 -> viewDataBinding.certificateImage7.setImage(file.toUri())
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApplicationData.files.clear()
        addProductImageRecyclerViewAdapter = AddProductImageRecyclerViewAdapter(selectedFileName, fileRequestFrom, galleryResultCallback, cameraResultCallback)

        viewDataBinding.recyclerView.run {
            adapter = addProductImageRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }

        productRequestModel?.let {
            if (it.price != -1) viewDataBinding.inputPrice.setText(it.price.toString())
            if (it.quantity != -1) viewDataBinding.inputAvailableQuantity.setText(it.quantity.toString())

            it.priceAndQuantityUnit?.run { viewDataBinding.inputSelectUnit.setText(this) }

            val mediaData = ArrayList<Pair<ProductMediaType, Any>>()
            if (!it.video.isNullOrEmpty()) it.video?.run { mediaData.add(Pair(ProductMediaType.VIDEO, this)) }
            if (!it.image1.isNullOrEmpty()) it.image1?.run { mediaData.add(Pair(ProductMediaType.IMAGE, this)) }
            if (!it.image2.isNullOrEmpty()) it.image2?.run { mediaData.add(Pair(ProductMediaType.IMAGE, this)) }
            if (!it.image3.isNullOrEmpty()) it.image3?.run { mediaData.add(Pair(ProductMediaType.IMAGE, this)) }
            if (!it.image4.isNullOrEmpty()) it.image4?.run { mediaData.add(Pair(ProductMediaType.IMAGE, this)) }
            if (!it.image5.isNullOrEmpty()) it.image5?.run { mediaData.add(Pair(ProductMediaType.IMAGE, this)) }

            addProductImageRecyclerViewAdapter.addData(mediaData)

            certificateImageMap.clear()
            it.certificateImage1?.let { img -> certificateImageMap[1] = Pair(CertificateImageType.URL, img) }
            it.certificateImage2?.let { img -> certificateImageMap[2] = Pair(CertificateImageType.URL, img) }
            it.certificateImage3?.let { img -> certificateImageMap[3] = Pair(CertificateImageType.URL, img) }
            it.certificateImage4?.let { img -> certificateImageMap[4] = Pair(CertificateImageType.URL, img) }
            it.certificateImage5?.let { img -> certificateImageMap[5] = Pair(CertificateImageType.URL, img) }
            it.certificateImage6?.let { img -> certificateImageMap[6] = Pair(CertificateImageType.URL, img) }
            it.certificateImage7?.let { img -> certificateImageMap[7] = Pair(CertificateImageType.URL, img) }

            certificateImageMap[1]?.second?.run {
                viewDataBinding.certificateImage1.setImage(BaseUrls.baseUrl + this, it.certificateStatue1 ?: 0)
                viewDataBinding.certificateStatus1.setCertificateStatus(it.certificateStatue1 ?: 0)
            }
            certificateImageMap[2]?.second?.run {
                viewDataBinding.certificateImage2.setImage(BaseUrls.baseUrl + this, it.certificateStatue2 ?: 0)
                viewDataBinding.certificateStatus2.setCertificateStatus(it.certificateStatue2 ?: 0)
            }
            certificateImageMap[3]?.second?.run {
                viewDataBinding.certificateImage3.setImage(BaseUrls.baseUrl + this, it.certificateStatue3 ?: 0)
                viewDataBinding.certificateStatus3.setCertificateStatus(it.certificateStatue3 ?: 0)
            }
            certificateImageMap[4]?.second?.run {
                viewDataBinding.certificateImage4.setImage(BaseUrls.baseUrl + this, it.certificateStatue4 ?: 0)
                viewDataBinding.certificateStatus4.setCertificateStatus(it.certificateStatue4 ?: 0)
            }
            certificateImageMap[5]?.second?.run {
                viewDataBinding.certificateImage5.setImage(BaseUrls.baseUrl + this, it.certificateStatue5 ?: 0)
                viewDataBinding.certificateStatus5.setCertificateStatus(it.certificateStatue5 ?: 0)
            }
            certificateImageMap[6]?.second?.run {
                viewDataBinding.certificateImage6.setImage(BaseUrls.baseUrl + this, it.certificateStatue6 ?: 0)
                viewDataBinding.certificateStatus6.setCertificateStatus(it.certificateStatue6 ?: 0)
            }
            certificateImageMap[7]?.second?.run {
                viewDataBinding.certificateImage7.setImage(BaseUrls.baseUrl + this, it.certificateStatue7 ?: 0)
                viewDataBinding.certificateStatus7.setCertificateStatus(it.certificateStatue7 ?: 0)
            }
        }

        viewDataBinding.unitList = resources.getStringArray(R.array.quantity_and_price_units).toCollection(ArrayList<String>())

        viewModel.getCertificateTypeList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.certificateListLiveData.observe(this, { list ->
            viewDataBinding.run {
                val certificateNameList = list.map { it.name }.toCollection(ArrayList())
                certificateName1.setText(list.firstOrNull { it.id == productRequestModel?.certificateTypeId1 }?.name)
                certificateName2.setText(list.firstOrNull { it.id == productRequestModel?.certificateTypeId2 }?.name)
                certificateName3.setText(list.firstOrNull { it.id == productRequestModel?.certificateTypeId3 }?.name)
                certificateName4.setText(list.firstOrNull { it.id == productRequestModel?.certificateTypeId4 }?.name)
                certificateName5.setText(list.firstOrNull { it.id == productRequestModel?.certificateTypeId5 }?.name)
                certificateName6.setText(list.firstOrNull { it.id == productRequestModel?.certificateTypeId6 }?.name)
                certificateName7.setText(list.firstOrNull { it.id == productRequestModel?.certificateTypeId7 }?.name)

                certificateImageMap[1]?.let { if(productRequestModel?.certificateStatue1 == 2) certificateName1.enableDropDown(certificateNameList) } ?: run { certificateName1.enableDropDown(certificateNameList) }
                certificateImageMap[2]?.let { if(productRequestModel?.certificateStatue2 == 2) certificateName2.enableDropDown(certificateNameList) } ?: run { certificateName2.enableDropDown(certificateNameList) }
                certificateImageMap[3]?.let { if(productRequestModel?.certificateStatue3 == 2) certificateName3.enableDropDown(certificateNameList) } ?: run { certificateName3.enableDropDown(certificateNameList) }
                certificateImageMap[4]?.let { if(productRequestModel?.certificateStatue4 == 2) certificateName4.enableDropDown(certificateNameList) } ?: run { certificateName4.enableDropDown(certificateNameList) }
                certificateImageMap[5]?.let { if(productRequestModel?.certificateStatue5 == 2) certificateName5.enableDropDown(certificateNameList) } ?: run { certificateName5.enableDropDown(certificateNameList) }
                //certificateImageMap[6]?.let { if(productRequestModel?.certificateStatue6 == 2) certificateName6.enableDropDown(certificateNameList) } ?: run { certificateName6.enableDropDown(certificateNameList) }
                //certificateImageMap[7]?.let { if(productRequestModel?.certificateStatue7 == 2) certificateName7.enableDropDown(certificateNameList) } ?: run { certificateName7.enableDropDown(certificateNameList) }
                certificateName6.enableDropDown(certificateNameList)
                certificateName7.enableDropDown(certificateNameList)
                executePendingBindings()
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            certificateImage1.setClickListener(certificateName1, 1)
            certificateImage2.setClickListener(certificateName2, 2)
            certificateImage3.setClickListener(certificateName3, 3)
            certificateImage4.setClickListener(certificateName4, 4)
            certificateImage5.setClickListener(certificateName5, 5)
            certificateImage6.setClickListener(certificateName6, 6)
            certificateImage7.setClickListener(certificateName7, 7)

            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            nextBtn.setOnClickListener {
                val media = addProductImageRecyclerViewAdapter.mediaSelected()
                val quantity = inputAvailableQuantity.text.toString().trim()
                val price = inputPrice.text.toString().trim()
                val unit = inputSelectUnit.text.toString().trim()

                validateFormData(quantity, price, media.isNotEmpty(), unit)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    ApplicationData.newProduct.priceAndQuantityUnit = unit
                    ApplicationData.newProduct.quantity = quantity.toInt()
                    ApplicationData.newProduct.price = price.toInt()

                    ApplicationData.newProduct.video = null
                    ApplicationData.newProduct.image1 = null
                    ApplicationData.newProduct.image2 = null
                    ApplicationData.newProduct.image3 = null
                    ApplicationData.newProduct.image4 = null
                    ApplicationData.newProduct.image5 = null

                    media.firstOrNull { it.first == ProductMediaType.VIDEO }?.let {
                        ApplicationData.newProduct.video = it.second as String
                    }

                    media.filter { it.first == ProductMediaType.IMAGE }.let {
                        for (index in it.indices) {
                            when (it[index].second) {
                                is File -> {
                                    ApplicationData.files.add(UtilityActions.multipartImage(it[index].second as File, "image_${index + 1}"))
                                }
                                is String -> {
                                    when (index) {
                                        0 -> ApplicationData.newProduct.image1 = it[index].second as String
                                        1 -> ApplicationData.newProduct.image2 = it[index].second as String
                                        2 -> ApplicationData.newProduct.image3 = it[index].second as String
                                        3 -> ApplicationData.newProduct.image4 = it[index].second as String
                                        4 -> ApplicationData.newProduct.image5 = it[index].second as String
                                    }
                                }
                            }
                        }
                    }

                    getCertificateData(1, certificateName1, { ApplicationData.newProduct.certificateImage1 = it }) {
                        ApplicationData.newProduct.certificateTypeId1 = it
                    }

                    getCertificateData(2, certificateName2, { ApplicationData.newProduct.certificateImage2 = it }) {
                        ApplicationData.newProduct.certificateTypeId2 = it
                    }

                    getCertificateData(3, certificateName3, { ApplicationData.newProduct.certificateImage3 = it }) {
                        ApplicationData.newProduct.certificateTypeId3 = it
                    }

                    getCertificateData(4, certificateName4, { ApplicationData.newProduct.certificateImage4 = it }) {
                        ApplicationData.newProduct.certificateTypeId4 = it
                    }

                    getCertificateData(5, certificateName5, { ApplicationData.newProduct.certificateImage5 = it }) {
                        ApplicationData.newProduct.certificateTypeId5 = it
                    }

                    getCertificateData(6, certificateName6, { ApplicationData.newProduct.certificateImage6 = it }) {
                        ApplicationData.newProduct.certificateTypeId6 = it
                    }

                    getCertificateData(7, certificateName7, { ApplicationData.newProduct.certificateImage7 = it }) {
                        ApplicationData.newProduct.certificateTypeId7 = it
                    }

                    val intent = Intent(this@ActivityAddProductStepTwo, ActivityAddProductStepThree::class.java)
                    intent.putExtra(Constant.PRODUCT_DATA, productRequestModel)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getCertificateData(index: Int, editText: EditText, certImageName: (String) -> Unit, certId: (Int) -> Unit) {
        certificateImageMap[index]?.let { pair ->
            when(pair.first) {
                CertificateImageType.FILE -> ApplicationData.files.add(UtilityActions.multipartImage(pair.second as File, "certificate_image_$index"))
                CertificateImageType.URL -> certImageName(pair.second as String)
            }

            val certificateName = editText.text.toString().trim()
            viewModel.certificateListLiveData.value?.firstOrNull { it.name == certificateName }?.let { certId(it.id) }
        }
    }

    private fun TextView.setCertificateStatus(status: Int) {
        this.run {
            visibility = View.VISIBLE
            text = when(status) {
                0 -> getString(R.string.pending)
                1 -> getString(R.string.verified)
                else -> getString(R.string.rejected)
            }
            setTextColor(ContextCompat.getColor(this@ActivityAddProductStepTwo, when(status) {
                0 -> R.color.orange
                1 -> R.color.green
                else -> R.color.red
            }))
        }
    }

    private fun ListItemAddProductImageBinding.setClickListener(inputCertificateName: EditText, index: Int) {
        this.run {
            productImg.setOnClickListener {
                certificateImageMap[index] ?: kotlin.run {
                    if(inputCertificateName.text.toString().trim().isNotEmpty()) {
                        fileRequestFrom.clear().append(ActivityAddProductStepTwo::class.simpleName)
                        selectedFileName.clear().append("${System.currentTimeMillis()}.jpg")
                        certificateIndex = index
                        showMediaDialog()
                    } else {
                        root.showMessage(getString(R.string.please_select_certificate_type))
                    }
                }
            }

            removeBtn.setOnClickListener {
                certificateImageMap.remove(index)
                productImg.setImageDrawable(ContextCompat.getDrawable(this@ActivityAddProductStepTwo, R.drawable.ic_baseline_add))
                removeBtn.visibility = View.GONE

                val certificateNameList = viewModel.certificateListLiveData.value?.map { it.name }?.toCollection(ArrayList())
                when(index) {
                    1 -> viewDataBinding.certificateName1.enableDropDown(certificateNameList)
                    2 -> viewDataBinding.certificateName2.enableDropDown(certificateNameList)
                    3 -> viewDataBinding.certificateName3.enableDropDown(certificateNameList)
                    4 -> viewDataBinding.certificateName4.enableDropDown(certificateNameList)
                    5 -> viewDataBinding.certificateName5.enableDropDown(certificateNameList)
                    6 -> viewDataBinding.certificateName6.enableDropDown(certificateNameList)
                    7 -> viewDataBinding.certificateName7.enableDropDown(certificateNameList)
                }
            }
        }
    }

    private fun ListItemAddProductImageBinding.setImage(uri: Uri) {
        this.productImg.setImageURI(uri)
        this.removeBtn.visibility = View.VISIBLE
    }

    private fun ListItemAddProductImageBinding.setImage(uri: String, status: Int) {
        this.productImg.loadImageFromNetwork(uri, null)
        this.removeBtn.visibility = if(status == 2) View.VISIBLE else View.GONE
    }

    private fun showMediaDialog() {
        MediaDialog(
            context = this,
            cameraFileName = selectedFileName.toString(),
            galleryCallback = galleryResultCallback,
            cameraCallback = cameraResultCallback,
            youtubeInputEnabled = false,
        ).show()
    }

    private fun validateFormData(quantity: String, price: String, isMediaSelected: Boolean, unit: String): String? {
        return if (quantity.isEmpty() || price.isEmpty() || unit.isEmpty()) getString(R.string.fill_all_the_fields)
        else if (!isMediaSelected) getString(R.string.add_at_least_one_image)
        else null
    }

    enum class CertificateImageType {
        FILE,
        URL,
    }
}