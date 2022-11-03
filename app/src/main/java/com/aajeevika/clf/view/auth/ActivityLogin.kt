package com.aajeevika.clf.view.auth

import android.content.Intent
import android.util.Patterns
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityLoginBinding
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions.debugOtp
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.utility.app_enum.VerificationType
import com.aajeevika.clf.view.auth.viewmodel.LoginViewModel
import com.aajeevika.clf.view.documents.ActivityDocuments
import com.aajeevika.clf.view.home.ActivityDashboard

class ActivityLogin : BaseActivityVM<ActivityLoginBinding, LoginViewModel>(
    R.layout.activity_login,
    LoginViewModel::class
) {
    override fun observeData() {
        super.observeData()

        viewModel.userProfileLiveData.observe(this, {
            if(it.is_otp_verified == 0) {
                debugOtp(it.otp)

                val intent = Intent(this, ActivityOtpVerification::class.java)
                intent.putExtra(Constant.VERIFICATION_TYPE, VerificationType.REGISTER.name)
                intent.putExtra(Constant.MOBILE_NUMBER, it.mobile)
                startActivity(intent)
            } else if(it.user?.api_token?.isNotEmpty() == true) {
                it.user?.run {
                    preferencesHelper.uid = id ?: -1
                    preferencesHelper.name = name ?: ""
                    preferencesHelper.title = title ?: ""
                    preferencesHelper.email = email ?: ""
                    preferencesHelper.mobile = mobile ?: ""
                    preferencesHelper.roleId = role_id ?: -1
                    preferencesHelper.authToken = api_token ?: ""
                    preferencesHelper.profileImage = profileImage ?: ""
                }

                if(it.user?.is_document_added == 0) {
                    val intent = Intent(this, ActivityDocuments::class.java)
                    intent.putExtra(Constant.USER_ROLE, preferencesHelper.roleId)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    val intent = Intent(this, ActivityDashboard::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            forgotPasswordBtn.setOnClickListener {
                val intent = Intent(this@ActivityLogin, ActivityEnterMobile::class.java)
                startActivity(intent)
            }

            loginBtn.setOnClickListener {
                val phoneOrEmail = viewDataBinding.inputEmail.text.toString().trim()
                val password = viewDataBinding.inputPassword.text.toString().trim()

                validateData(phoneOrEmail, password)?.let {
                    viewDataBinding.root.showMessage(it)
                } ?: run {
                    viewModel.requestUserLogin(phoneOrEmail, password)
                }
            }

            clickToRegisterBtn.setOnClickListener {
                val intent = Intent(this@ActivityLogin, ActivityRegister::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateData(phoneOrEmail: String, password: String): String? = run {
        when {
            phoneOrEmail.isEmpty() -> getString(R.string.enter_mobile_or_email)
            !Patterns.PHONE.matcher(phoneOrEmail).matches() && !Patterns.EMAIL_ADDRESS.matcher(phoneOrEmail).matches() -> getString(R.string.invalid_mobile_or_email)
            password.isEmpty() -> getString(R.string.enter_password)
            password.length < 8 -> getString(R.string.password_length_error)
            else -> null
        }
    }
}