package com.aajeevika.clf.view.profile

import BaseUrls
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityProfileBinding
import com.aajeevika.clf.model.data_model.AddressData
import com.aajeevika.clf.model.data_model.UserData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.app_enum.DocumentType
import com.aajeevika.clf.view.application.ActivityRatingAndReviews
import com.aajeevika.clf.view.dialog.ImagePreviewDialog
import com.aajeevika.clf.view.dialog.MediaDialog
import com.aajeevika.clf.view.documents.ActivityDocuments
import com.aajeevika.clf.view.profile.viewmodel.ProfileViewModel
import com.yalantis.ucrop.UCrop
import java.io.File
import java.lang.StringBuilder

class ActivityProfile : BaseActivityVM<ActivityProfileBinding, ProfileViewModel>(
    R.layout.activity_profile,
    ProfileViewModel::class
) {
    private lateinit var addressData: AddressData
    private lateinit var userData: UserData

    private val galleryResultCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            val file = File(cacheDir, Constant.USER_PROFILE)
            if(!file.exists()) file.createNewFile()

            UtilityActions.startUcropActivityForResult(
                this, it, file.toUri(), uCropCallBack, 1F, 1F, true
            )
        }
    }

    private val cameraResultCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it == true) {
            val file = File(cacheDir, Constant.USER_PROFILE)

            UtilityActions.startUcropActivityForResult(
                this, file.toUri(), file.toUri(), uCropCallBack, 1F, 1F, true
            )
        }
    }

    private val uCropCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.data?.let { intent ->
            UCrop.getOutput(intent)?.toFile()?.let { file ->
                val profileImage = UtilityActions.multipartImage(file, "profileimage")
                viewModel.uploadProfileImage(profileImage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateUserProfile()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUserProfile()
    }

    override fun observeData() {
        super.observeData()

        viewModel.userLiveData.observe(this, {
            it.user?.run {
                userData = this

                preferencesHelper.name = name ?: ""
                preferencesHelper.title = title ?: ""
                preferencesHelper.email = email ?: ""
                preferencesHelper.mobile = mobile ?: ""
                preferencesHelper.memberId = member_id ?: ""
                preferencesHelper.profileImage = profileImage ?: ""
                preferencesHelper.memberDesignation = member_designation ?: ""
                preferencesHelper.organisationName = organization_name ?: ""
            }

            it.address?.registered?.run {
                addressData = this

                val addressStringBuilder = StringBuilder()
                address_line_one?.run { addressStringBuilder.append("$this, ") }
                address_line_two?.run { addressStringBuilder.append("$this, ") }
                district?.run { addressStringBuilder.append("$this, ") }
                state?.run { addressStringBuilder.append("$this, ") }
                country?.run { addressStringBuilder.append("$this, ") }
                block?.run { addressStringBuilder.append("$this, ") }
                pincode?.run { addressStringBuilder.append(this) }

                preferencesHelper.address = addressStringBuilder.toString()
            }

            updateUserProfile()

            viewDataBinding.run {
                totalRating = it.rating?.reviewCount?.toInt() ?: 0
                rating = it.rating?.ratingAvgStar?.toFloat() ?: 0F

                isAadharVerified = it.user?.is_adhar_verify == 1
                isPanVerified = it.user?.is_pan_verify == 1
                isBrnVerified = it.user?.is_brn_verify == 1

                isAadharAdded = it.user?.is_aadhar_added == 1
                isPanAdded = it.user?.is_pan_added == 1
                isBrnAdded = it.user?.is_brn_added == 1
                executePendingBindings()
            }
        })

        viewModel.statusLiveData.observe(this, {
            viewModel.requestUserProfile()
        })
    }

    private fun updateUserProfile() {
        viewDataBinding.run {
            mobileNumber = preferencesHelper.mobile
            userName = preferencesHelper.name
            emailId = preferencesHelper.email
            memberId = preferencesHelper.memberId
            designation = preferencesHelper.memberDesignation
            organizationName = preferencesHelper.organisationName
            address = preferencesHelper.address
            userType = Constant.getUserTypes(this@ActivityProfile).firstOrNull { it.first == preferencesHelper.roleId }?.second

            if(preferencesHelper.profileImage.isNotEmpty())
                profileImage = BaseUrls.baseUrl + preferencesHelper.profileImage
        }
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            toolbar.editBtn.setOnClickListener {
                val intent = Intent(this@ActivityProfile, ActivityEditProfile::class.java)
                intent.putExtra(Constant.ADDRESS, addressData)
                startActivity(intent)
            }

            ratingBtn.setOnClickListener {
                val intent = Intent(this@ActivityProfile, ActivityRatingAndReviews::class.java)
                startActivity(intent)
            }

            docTypeAadhar.run {
                verificationStatus.setOnClickListener {
                    if(isAadharAdded && !isAadharVerified) uploadDocument(DocumentType.AADHAR)
                }

                documentBtn.setOnClickListener {
                    userData.adhar_card_front_file?.let { frontFile ->
                        userData.adhar_card_back_file?.let { backFile ->
                            ImagePreviewDialog(this@ActivityProfile, BaseUrls.baseUrl + frontFile, BaseUrls.baseUrl + backFile).show()
                        }
                    }
                }

                uploadDocumentBtn.setOnClickListener {
                    uploadDocument(DocumentType.AADHAR)
                }
            }

            docTypePan.run {
                verificationStatus.setOnClickListener {
                    if(isPanAdded && !isPanVerified) uploadDocument(DocumentType.PAN)
                }

                documentBtn.setOnClickListener {
                    userData.pancard_file?.let { file ->
                        ImagePreviewDialog(this@ActivityProfile, BaseUrls.baseUrl + file).show()
                    }
                }

                uploadDocumentBtn.setOnClickListener {
                    uploadDocument(DocumentType.PAN)
                }
            }

            docTypeBrn.run {
                verificationStatus.setOnClickListener {
                    if(isBrnAdded && !isBrnVerified) uploadDocument(DocumentType.BRN)
                }

                documentBtn.setOnClickListener {
                    userData.brn_file?.let { file ->
                        ImagePreviewDialog(this@ActivityProfile, BaseUrls.baseUrl + file).show()
                    }
                }

                uploadDocumentBtn.setOnClickListener {
                    uploadDocument(DocumentType.BRN)
                }
            }

            editProfileBtn.setOnClickListener {
                MediaDialog(
                    context = this@ActivityProfile,
                    cameraFileName = Constant.USER_PROFILE,
                    galleryCallback = galleryResultCallback,
                    cameraCallback = cameraResultCallback,
                ).show()
            }
        }
    }

    private fun uploadDocument(documentType: DocumentType) {
        val intent = Intent(this@ActivityProfile, ActivityDocuments::class.java)
        intent.putExtra(Constant.USER_ROLE, preferencesHelper.roleId)
        intent.putExtra(Constant.DOCUMENT_TYPE, documentType.name)
        startActivity(intent)
    }
}