package com.aajeevika.clf.view.profile

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityEditProfileBinding
import com.aajeevika.clf.model.data_model.AddressData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.utility.app_enum.VerificationType
import com.aajeevika.clf.view.auth.ActivityEnterMobile
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.profile.viewmodel.ProfileViewModel

class ActivityEditProfile : BaseActivityVM<ActivityEditProfileBinding, ProfileViewModel>(
    R.layout.activity_edit_profile,
    ProfileViewModel::class
) {
    private val addressData by lazy { intent.getSerializableExtra(Constant.ADDRESS) as? AddressData }

    val editAddressCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) finish()
    }

    override fun onStart() {
        super.onStart()

        viewDataBinding.run {
            inputNameOfOrganisation.setText(preferencesHelper.organisationName)
            inputMemberName.setText(preferencesHelper.name)
            inputMemberDesignation.setText(preferencesHelper.memberDesignation)
            inputMemberId.setText(preferencesHelper.memberId)
            inputEmail.setText(preferencesHelper.email)
            inputMobileNumber.setText(preferencesHelper.mobile)
            inputAddress.setText(preferencesHelper.address)

            memberDesignationList = resources.getStringArray(R.array.designation_list).toCollection(ArrayList<String>())
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.statusLiveData.observe(this, {
            AlertDialog(
                context = this,
                cancelOnOutsideClick = false,
                message = it,
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

            saveBtn.setOnClickListener {
                val organisationName = inputNameOfOrganisation.text.toString().trim()
                val memberDesignation = inputMemberDesignation.text.toString().trim()
                val memberName = inputMemberName.text.toString().trim()
                val memberId = inputMemberId.text.toString().trim()
                val email = inputEmail.text.toString().trim()

                validateFormData(organisationName, memberDesignation, memberName)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    viewModel.updateProfileData(memberName, memberId, memberDesignation, organisationName, email)
                }
            }

            changeMobileBtn.setOnClickListener {
                val intent = Intent(this@ActivityEditProfile, ActivityEnterMobile::class.java)
                intent.putExtra(Constant.VERIFICATION_TYPE, VerificationType.CHANGE_MOBILE_NUMBER.name)
                startActivity(intent)
            }

            changeAddressBtn.setOnClickListener {
                val intent = Intent(this@ActivityEditProfile, ActivityChangeAddress::class.java)
                intent.putExtra(Constant.ADDRESS, addressData)
                editAddressCallback.launch(intent)
            }
        }
    }

    private fun validateFormData(organisationName: String, memberDesignation: String, memberName: String) : String? {
        return if(organisationName.isEmpty() || memberDesignation.isEmpty() || memberName.isEmpty()) getString(R.string.fill_all_the_fields)
        else null
    }
}