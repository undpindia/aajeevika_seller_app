package com.aajeevika.clf.view.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityOtpVerificationBinding
import com.aajeevika.clf.utility.*
import com.aajeevika.clf.utility.UtilityActions.debugOtp
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.utility.app_enum.VerificationType
import com.aajeevika.clf.view.auth.viewmodel.OtpVerificationViewModel
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.documents.ActivityDocuments
import com.aajeevika.clf.view.home.ActivityDashboard

class ActivityOtpVerification : BaseActivityVM<ActivityOtpVerificationBinding, OtpVerificationViewModel>(
    R.layout.activity_otp_verification,
    OtpVerificationViewModel::class
) {
    private lateinit var countDownTimer: CountDownTimer
    private val mobileNumber: String by lazy { intent.getStringExtra(Constant.MOBILE_NUMBER) ?: "" }

    private val verificationType: VerificationType by lazy {
        intent.getStringExtra(Constant.VERIFICATION_TYPE)?.let { VerificationType.valueOf(it) } ?: VerificationType.FORGOT_PASSWORD
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.title = getString(R.string.verify_contact)
        viewDataBinding.message = String.format(getString(R.string.enter_4_digit_otp_sent_to_s), mobileNumber)

        initiateTimer()
    }

    override fun onResume() {
        super.onResume()
        UtilityActions.openKeyboard(this, viewDataBinding.inputOtpView)
    }

    override fun onDestroy() {
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
        super.onDestroy()
    }

    override fun observeData() {
        super.observeData()
        viewModel.otpLiveData.observe(this, {
            debugOtp(it.otp)
        })

        viewModel.userProfileLiveData.observe(this, {
            println("Verification Type: ${verificationType.name}")

            when(verificationType) {
                VerificationType.FORGOT_PASSWORD -> {
                    val intent = Intent(this, ActivityResetPassword::class.java)
                    intent.putExtra(Constant.OTP, viewDataBinding.inputOtpView.text.toString().toInt())
                    intent.putExtra(Constant.MOBILE_NUMBER, mobileNumber)
                    startActivity(intent)
                    finish()
                }
                VerificationType.CHANGE_MOBILE_NUMBER -> {
                    viewModel.updateMobileNumber(mobileNumber, viewDataBinding.inputOtpView.text.toString().toInt())
                }
                VerificationType.REGISTER -> {
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

                    val intent = Intent(this, ActivityDocuments::class.java)
                    intent.putExtra(Constant.USER_ROLE, preferencesHelper.roleId)
                    startActivity(intent)
                    finish()
                }
            }
        })

        viewModel.updateMobileLiveData.observe(this, {
            AlertDialog(
                context = this,
                cancelOnOutsideClick = true,
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

            resendTxt.setOnClickListener {
                viewDataBinding.inputOtpView.text = null
                viewModel.requestOtpResend(mobileNumber)
                initiateTimer()
            }

            verifyBtn.setOnClickListener {
                val pin = viewDataBinding.inputOtpView.text.toString().trim()

                if (pin.length == 4)
                    viewModel.requestOtpVerification(mobileNumber, pin, verificationType == VerificationType.REGISTER)
                else
                    viewDataBinding.root.showMessage(getString(R.string.enter_valid_otp))
            }

            inputOtpView.doOnTextChanged { pin, _, _, _ ->
                if ((pin?.length ?: 0) == 4) {
                    UtilityActions.closeKeyboard(this@ActivityOtpVerification, inputOtpView)
                }
            }
        }
    }

    private fun initiateTimer() {
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onFinish() = handleCountdown(-1)
            override fun onTick(millisUntilFinished: Long) = handleCountdown((millisUntilFinished / 1000).toInt() + 1)
        }.start()
    }

    private fun handleCountdown(countdown: Int) {
        if (countdown == -1) {
            viewDataBinding.timerTxt.visibility = View.INVISIBLE
            viewDataBinding.resendTxt.visibility = View.VISIBLE
        } else {
            viewDataBinding.timerTxt.visibility = View.VISIBLE
            viewDataBinding.resendTxt.visibility = View.INVISIBLE
            viewDataBinding.timerTxt.text = String.format(getString(R.string.remaining_d_sec), countdown)
        }
    }
}

