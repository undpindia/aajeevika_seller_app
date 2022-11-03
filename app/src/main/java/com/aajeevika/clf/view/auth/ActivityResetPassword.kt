package com.aajeevika.clf.view.auth

import android.content.Intent
import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityResetPasswordBinding
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.view.auth.viewmodel.ResetPasswordViewModel
import com.aajeevika.clf.view.dialog.AlertDialog

class ActivityResetPassword : BaseActivityVM<ActivityResetPasswordBinding, ResetPasswordViewModel>(
    R.layout.activity_reset_password,
    ResetPasswordViewModel::class
) {
    private val mobileNo by lazy { intent.getStringExtra(Constant.MOBILE_NUMBER) ?: "" }
    private val otp by lazy { intent.getIntExtra(Constant.OTP, -1) }

    override fun observeData() {
        super.observeData()

        viewModel.requestStatus.observe(this, {
            AlertDialog(
                context = this,
                cancelOnOutsideClick = false,
                message = it,
                positive = getString(R.string.ok),
                positiveClick = {
                    val intent = Intent(this, ActivityLogin::class.java)
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

            saveBtn.setOnClickListener {
                val password = viewDataBinding.inputPassword.text.toString().trim()
                val confirmPassword = viewDataBinding.inputConfirmPassword.text.toString().trim()

                validateFormData(password, confirmPassword)?.let { error ->
                    viewDataBinding.root.showMessage(error)
                } ?: run {
                    viewModel.requestPasswordChange(mobileNo, password,otp)
                }
            }
        }
    }

    private fun validateFormData(password: String, confirmPassword: String) : String? {
        return when {
            password.isEmpty() -> getString(R.string.enter_new_password)
            password.length < 8 -> getString(R.string.password_length_error)
            password != confirmPassword -> getString(R.string.password_doesnt_match)
            else -> null
        }
    }
}