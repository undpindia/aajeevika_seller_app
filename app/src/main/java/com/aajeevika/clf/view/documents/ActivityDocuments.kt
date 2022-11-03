package com.aajeevika.clf.view.documents

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityDocumentsBinding
import com.aajeevika.clf.location.activity.MapsActivity
import com.aajeevika.clf.location.constants.EXTRA_LATITUDE
import com.aajeevika.clf.location.constants.EXTRA_LONGITUDE
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.utility.app_enum.DocumentType
import com.aajeevika.clf.view.auth.ActivityLogin
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.dialog.MediaDialog
import com.aajeevika.clf.view.documents.viewmodel.DocumentsViewModel
import com.aajeevika.clf.view.home.ActivityDashboard
import com.yalantis.ucrop.UCrop
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityDocuments : BaseActivityVM<ActivityDocumentsBinding, DocumentsViewModel>(
    R.layout.activity_documents,
    DocumentsViewModel::class
) {
    private lateinit var brnImage: File
    private lateinit var panImage: File
    private lateinit var aadharBackImage: File
    private lateinit var aadharFrontImage: File
    val files = ArrayList<MultipartBody.Part>()
    val fieldMap = HashMap<String, RequestBody>()

    private val documentType by lazy { intent.getStringExtra(Constant.DOCUMENT_TYPE)?.let { DocumentType.valueOf(it) } }
    private val roleId by lazy { intent.getIntExtra(Constant.USER_ROLE, -1) }

    private val aadharFrontGalleryCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            val file = File(cacheDir, Constant.AADHAR_CARD_FRONT)
            if(!file.exists()) file.createNewFile()

            UtilityActions.startUcropActivityForResult(this, it, file.toUri(), uCropCallBack, 3.3F, 2.1F)
        }
    }

    private val aadharFrontCameraCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess == true) {
            val file = File(cacheDir, Constant.AADHAR_CARD_FRONT)

            UtilityActions.startUcropActivityForResult(this, file.toUri(), file.toUri(), uCropCallBack, 3.3F, 2.1F)
        }
    }

    private val aadharBackGalleryCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            val file = File(cacheDir, Constant.AADHAR_CARD_BACK)
            if(!file.exists()) file.createNewFile()

            UtilityActions.startUcropActivityForResult(this, it, file.toUri(), uCropCallBack, 3.3F, 2.1F)
        }
    }

    private val aadharBackCameraCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess == true) {
            val file = File(cacheDir, Constant.AADHAR_CARD_BACK)

            UtilityActions.startUcropActivityForResult(this, file.toUri(), file.toUri(), uCropCallBack, 3.3F, 2.1F)
        }
    }

    private val panGalleryCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            val file = File(cacheDir, Constant.PAN_CARD)
            if(!file.exists()) file.createNewFile()

            UtilityActions.startUcropActivityForResult(this, it, file.toUri(), uCropCallBack, 3.3F, 2.1F)
        }
    }

    private val panCameraCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess == true) {
            val file = File(cacheDir, Constant.PAN_CARD)

            UtilityActions.startUcropActivityForResult(this, file.toUri(), file.toUri(), uCropCallBack, 3.3F, 2.1F)
        }
    }

    private val brnGalleryCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            val file = File(cacheDir, Constant.BRN_CARD)
            if(!file.exists()) file.createNewFile()

            UtilityActions.startUcropActivityForResult(this, it, file.toUri(), uCropCallBack, 3.3F, 2.1F)
        }
    }

    private val brnCameraCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess == true) {
            val file = File(cacheDir, Constant.BRN_CARD)

            UtilityActions.startUcropActivityForResult(this, file.toUri(), file.toUri(), uCropCallBack, 3.3F, 2.1F)
        }
    }

    private val uCropCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.data?.let { intent ->
            UCrop.getOutput(intent)?.toFile()?.let { file ->
                when (file.name) {
                    Constant.BRN_CARD -> {
                        brnImage = file
                        viewDataBinding.brnCard.uploadBrnTxt.text = file.name
                    }
                    Constant.PAN_CARD -> {
                        panImage = file
                        viewDataBinding.panCard.uploadPanTxt.text = file.name
                    }
                    Constant.AADHAR_CARD_BACK -> {
                        aadharBackImage = file
                        viewDataBinding.aadharCard.uploadAadharBackTxt.text = file.name
                    }
                    Constant.AADHAR_CARD_FRONT -> {
                        aadharFrontImage = file
                        viewDataBinding.aadharCard.uploadAadharFrontTxt.text = file.name
                    }
                }
            }
        }
    }

    private val mapResultCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) {
            it.data?.let { data ->
                fieldMap["lat"] = data.getDoubleExtra(EXTRA_LATITUDE, 0.0).toString().toRequestBody()
                fieldMap["log"] = data.getDoubleExtra(EXTRA_LONGITUDE, 0.0).toString().toRequestBody()

                viewModel.uploadDocuments(roleId, fieldMap, files)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        documentType?.let {
            viewDataBinding.run {
                aadharCard.root.visibility = View.GONE
                panCard.root.visibility = View.GONE
                brnCard.root.visibility = View.GONE
                registeredAddress.root.visibility = View.GONE

                when(it) {
                    DocumentType.AADHAR -> {
                        aadharCard.isRequired = true
                        aadharCard.root.visibility = View.VISIBLE
                    }
                    DocumentType.PAN -> {
                        panCard.isRequired = true
                        panCard.root.visibility = View.VISIBLE
                    }
                    DocumentType.BRN -> {
                        brnCard.isRequired = true
                        brnCard.root.visibility = View.VISIBLE
                    }
                }
            }
        }

        if(documentType == null) {
            viewModel.run {
                getState()
                getDistrict()
                getCountries()
            }
        }
    }

    override fun onBackPressed() {
        documentType?.run { super.onBackPressed() } ?: run {
            val intent = Intent(this@ActivityDocuments, ActivityLogin::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.countryLiveData.observe(this, { list ->
            viewDataBinding.country = list.firstOrNull { it.id == Constant.DEFAULT_COUNTRY_ID }?.name
        })

        viewModel.stateLiveData.observe(this, { list ->
            viewDataBinding.state = list.firstOrNull { it.id == Constant.DEFAULT_STATE_ID }?.name
        })

        viewModel.districtLiveData.observe(this, { list ->
            viewDataBinding.districtList = list.map { it.name }.toCollection(ArrayList<String>())
        })

        viewModel.blockLiveData.observe(this, { list ->
            viewDataBinding.blockList = list.map { it.name }.toCollection(ArrayList<String>())
        })

        viewModel.requestStatusLiveData.observe(this, {
            documentType?.run {
                AlertDialog(
                    context = this@ActivityDocuments,
                    cancelOnOutsideClick = false,
                    message = it,
                    positive = getString(R.string.ok),
                    positiveClick = { onBackPressed() }
                ).show()
            } ?: run {
                val intent = Intent(this@ActivityDocuments, ActivityDashboard::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            backBtn.setOnClickListener {
                onBackPressed()
            }

            registeredAddress.inputDistrict.doOnTextChanged { text, _, _, _ ->
                registeredAddress.inputBlock.text = null

                text?.toString()?.trim()?.let { district ->
                    viewModel.districtLiveData.value?.firstOrNull { it.name == district }?.id?.let {
                        viewModel.getBlock(it)
                    }
                }
            }

            aadharCard.run {
                uploadAadharFrontBtn.setOnClickListener {
                    MediaDialog(
                        context = this@ActivityDocuments,
                        cameraFileName = Constant.AADHAR_CARD_FRONT,
                        galleryCallback = aadharFrontGalleryCallback,
                        cameraCallback = aadharFrontCameraCallback,
                    ).show()
                }

                uploadAadharBackBtn.setOnClickListener {
                    MediaDialog(
                        context = this@ActivityDocuments,
                        cameraFileName = Constant.AADHAR_CARD_BACK,
                        galleryCallback = aadharBackGalleryCallback,
                        cameraCallback = aadharBackCameraCallback,
                    ).show()
                }

                inputAadharDob.setOnClickListener {
                    val calendar = Calendar.getInstance()

                    val aadharDOB = aadharCard.inputAadharDob.text.toString().trim()
                    UtilityActions.parseDate(aadharDOB)?.let { calendar.time = it }

                    DatePickerDialog(
                        this@ActivityDocuments,
                        { _, year, month, dayOfMonth ->
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.YEAR, year)

                            inputAadharDob.setText(UtilityActions.formatDate(calendar.time))
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                    ).show()
                }
            }

            panCard.run {
                uploadPanBtn.setOnClickListener {
                    MediaDialog(
                        context = this@ActivityDocuments,
                        cameraFileName = Constant.PAN_CARD,
                        galleryCallback = panGalleryCallback,
                        cameraCallback = panCameraCallback,
                    ).show()
                }

                inputPanDob.setOnClickListener {
                    val calendar = Calendar.getInstance()

                    val panDOB = panCard.inputPanDob.text.toString().trim()
                    UtilityActions.parseDate(panDOB)?.let { calendar.time = it }

                    DatePickerDialog(
                        this@ActivityDocuments,
                        { _, year, month, dayOfMonth ->
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.YEAR, year)

                            inputPanDob.setText(UtilityActions.formatDate(calendar.time))
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                    ).show()
                }
            }

            brnCard.run {
                uploadBrnBtn.setOnClickListener {
                    MediaDialog(
                        context = this@ActivityDocuments,
                        cameraFileName = Constant.BRN_CARD,
                        galleryCallback = brnGalleryCallback,
                        cameraCallback = brnCameraCallback,
                    ).show()
                }
            }

            verifyBtn.setOnClickListener {
                if(aadharCard.root.visibility == View.VISIBLE) validateAadharInfo()?.run {
                    root.showMessage(this)
                    return@setOnClickListener
                } ?: run {
                    files.clear()
                    fieldMap.clear()

                    val aadharNumber = aadharCard.inputAadahrNumber.text.toString().trim()
                    val aadharName = aadharCard.inputAadharName.text.toString().trim()
                    val aadharDOB = aadharCard.inputAadharDob.text.toString().trim()

                    if(aadharNumber.isNotEmpty()) fieldMap["adhar_card_no"] = aadharNumber.toRequestBody()
                    if(aadharName.isNotEmpty()) fieldMap["adhar_name"] = aadharName.toRequestBody()
                    if(aadharDOB.isNotEmpty()) fieldMap["adhar_dob"] = aadharDOB.toRequestBody()
                    if(::aadharFrontImage.isInitialized) files.add(UtilityActions.multipartImage(aadharFrontImage, "adhar_card_front_file"))
                    if(::aadharBackImage.isInitialized) files.add(UtilityActions.multipartImage(aadharBackImage, "adhar_card_back_file"))
                }

                if(panCard.root.visibility == View.VISIBLE) validatePanInfo()?.run {
                    root.showMessage(this)
                    return@setOnClickListener
                } ?: run {
                    val panNumber = panCard.inputPanNumber.text.toString().trim()
                    val panName = panCard.inputPanName.text.toString().trim()
                    val panDOB = panCard.inputPanDob.text.toString().trim()

                    if(panNumber.isNotEmpty()) fieldMap["pancard_no"] = panNumber.toRequestBody()
                    if(panName.isNotEmpty()) fieldMap["pancard_name"] = panName.toRequestBody()
                    if(panDOB.isNotEmpty()) fieldMap["pancard_dob"] = panDOB.toRequestBody()
                    if(::panImage.isInitialized) files.add(UtilityActions.multipartImage(panImage, "pancard_file"))
                }

                if(brnCard.root.visibility == View.VISIBLE) validateBrnInfo()?.run {
                    root.showMessage(this)
                    return@setOnClickListener
                } ?: run {
                    val brnNumber = brnCard.inputBrnNumber.text.toString().trim()
                    val brnName = brnCard.inputBrnName.text.toString().trim()

                    if(brnNumber.isNotEmpty()) fieldMap["brn_no"] = brnNumber.toRequestBody()
                    if(brnName.isNotEmpty()) fieldMap["brn_name"] = brnName.toRequestBody()
                    if(::brnImage.isInitialized) files.add(UtilityActions.multipartImage(brnImage, "brn_file"))
                }

                if(registeredAddress.root.visibility == View.VISIBLE) validateRegisteredAddressInfo()?.run {
                    root.showMessage(this)
                    return@setOnClickListener
                } ?: run {
                    val addressLineOne = registeredAddress.inputAddressLineOne.text.toString().trim()
                    val addressLineTwo = registeredAddress.inputAddressLineTwo.text.toString().trim()
                    val district = registeredAddress.inputDistrict.text.toString().trim()
                    val pin = registeredAddress.inputPin.text.toString().trim()
                    val block = registeredAddress.inputBlock.text.toString().trim()

                    val districtId = viewModel.districtLiveData.value?.firstOrNull { it.name == district }?.id ?: -1
                    val blockId = viewModel.blockLiveData.value?.firstOrNull { it.name == block }?.id ?: -1

                    fieldMap["address_line_one_registered"] = addressLineOne.toRequestBody()
                    if(addressLineTwo.isNotEmpty()) fieldMap["address_line_two_registered"] = addressLineTwo.toRequestBody()
                    fieldMap["country_registered"] = Constant.DEFAULT_COUNTRY_ID.toString().toRequestBody()
                    fieldMap["state_registered"] = Constant.DEFAULT_STATE_ID.toString().toRequestBody()
                    fieldMap["district_registered"] = districtId.toString().toRequestBody()
                    fieldMap["pincode_registered"] = pin.toRequestBody()
                    fieldMap["block"] = blockId.toString().toRequestBody()
                }

                documentType?.run {
                    fieldMap["is_adhar_update"] = (if(this == DocumentType.AADHAR) 1 else 0).toString().toRequestBody()
                    fieldMap["is_pan_update"] = (if(this == DocumentType.PAN) 1 else 0).toString().toRequestBody()
                    fieldMap["is_brn_update"] = (if(this == DocumentType.BRN) 1 else 0).toString().toRequestBody()

                    viewModel.reUploadDocument(roleId, fieldMap, files)
                } ?: run {
                    val addressLineOne = registeredAddress.inputAddressLineOne.text.toString().trim()
                    val addressLineTwo = registeredAddress.inputAddressLineTwo.text.toString().trim()
                    val country = registeredAddress.inputCountry.text.toString().trim()
                    val state = registeredAddress.inputState.text.toString().trim()
                    val district = registeredAddress.inputDistrict.text.toString().trim()
                    val pin = registeredAddress.inputPin.text.toString().trim()
                    val block = registeredAddress.inputBlock.text.toString().trim()

                    val formattedAddress = "$addressLineOne, $addressLineTwo, $block, $district, $state, $country - $pin"

                    val intent = Intent(this@ActivityDocuments, MapsActivity::class.java)
                    intent.putExtra(Constant.ADDRESS, formattedAddress)
                    mapResultCallback.launch(intent)
                }
            }
        }
    }

    private fun validateAadharInfo(): String? {
        viewDataBinding.aadharCard.run {
            val aadharNumber = inputAadahrNumber.text.toString().trim()
            val aadharName = inputAadharName.text.toString().trim()
            val aadharDOB = inputAadharDob.text.toString().trim()

            if(documentType == null) {
                if(
                    aadharNumber.isEmpty() &&
                    aadharName.isEmpty() &&
                    aadharDOB.isEmpty() &&
                    !::aadharFrontImage.isInitialized &&
                    !::aadharBackImage.isInitialized
                ) return null
            }

            return if (aadharNumber.length != 12) getString(R.string.enter_valid_aadhar_number)
            else if (aadharName.isEmpty()) getString(R.string.enter_valid_aadhar_name)
            else if (aadharDOB.isEmpty()) getString(R.string.enter_valid_aadhar_dob)
            else if (!::aadharFrontImage.isInitialized) getString(R.string.enter_valid_aadhar_front_image)
            else if (!::aadharBackImage.isInitialized) getString(R.string.enter_valid_aadhar_back_image)
            else null
        }
    }

    private fun validatePanInfo(): String? {
        viewDataBinding.panCard.run {
            val panNumber = inputPanNumber.text.toString().trim()
            val panName = inputPanName.text.toString().trim()
            val panDOB = inputPanDob.text.toString().trim()

            if(documentType == null) {
                if(
                    panNumber.isEmpty() &&
                    panName.isEmpty() &&
                    panDOB.isEmpty() &&
                    !::panImage.isInitialized
                ) return null
            }

            return if (panNumber.length != 10) getString(R.string.enter_valid_pan_number)
            else if (panName.isEmpty()) getString(R.string.enter_valid_pan_name)
            else if (panDOB.isEmpty()) getString(R.string.enter_valid_pan_dob)
            else if (!::panImage.isInitialized) getString(R.string.enter_pan_image)
            else null
        }
    }

    private fun validateBrnInfo(): String? {
        viewDataBinding.brnCard.run {
            val brnNumber = inputBrnNumber.text.toString().trim()
            val brnName = inputBrnName.text.toString().trim()

            if(documentType == null) {
                if(
                    brnNumber.isEmpty() &&
                    brnName.isEmpty() &&
                    !::brnImage.isInitialized
                ) return null
            }

            return if (brnNumber.length != 12) getString(R.string.enter_valid_brn_number)
            else if (brnName.isEmpty()) getString(R.string.enter_valid_brn_name)
            else if (!::brnImage.isInitialized) getString(R.string.enter_valid_brn_image)
            else null
        }
    }

    private fun validateRegisteredAddressInfo() : String? {
        viewDataBinding.registeredAddress.run {
            val addressLineOne = inputAddressLineOne.text.toString().trim()
            val country = inputCountry.text.toString().trim()
            val state = inputState.text.toString().trim()
            val district = inputDistrict.text.toString().trim()
            val pin = inputPin.text.toString().trim()
            val block = inputBlock.text.toString().trim()

            return when {
                addressLineOne.isEmpty() -> getString(R.string.address_line_1)
                country.isEmpty() -> getString(R.string.enter_country)
                state.isEmpty() -> getString(R.string.enter_state)
                district.isEmpty() -> getString(R.string.enter_city)
                pin.length != 6 -> getString(R.string.enter_pin)
                block.isEmpty() -> getString(R.string.enter_block)
                else -> null
            }
        }
    }
}