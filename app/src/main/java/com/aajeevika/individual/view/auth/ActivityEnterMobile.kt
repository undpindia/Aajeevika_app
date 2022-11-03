package com.aajeevika.individual.view.auth

import android.content.Intent
import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityEnterMobileBinding
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.utility.UtilityActions.debugOtp
import com.aajeevika.individual.utility.UtilityActions.showMessage
import com.aajeevika.individual.utility.app_enum.VerificationType
import com.aajeevika.individual.view.auth.viewmodel.ForgotPasswordViewModel

class ActivityEnterMobile : BaseActivityVM<ActivityEnterMobileBinding, ForgotPasswordViewModel>(
    R.layout.activity_enter_mobile,
    ForgotPasswordViewModel::class
) {
    private val verificationType by lazy { intent.getStringExtra(Constant.VERIFICATION_TYPE)?.let { VerificationType.valueOf(it) } }
    var mobileNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.run {
            title = when(verificationType) {
                VerificationType.CHANGE_MOBILE_NUMBER -> getString(R.string.change_mobile)
                else -> getString(R.string.forgot_password)
            }

            message = when(verificationType) {
                VerificationType.CHANGE_MOBILE_NUMBER -> getString(R.string.change_mobile_number_message)
                else -> getString(R.string.forgot_password_message)
            }
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.otpLiveData.observe(this, {
            debugOtp(it.otp)

            val intent = Intent(this@ActivityEnterMobile, ActivityOtpVerification::class.java)
            intent.putExtra(Constant.VERIFICATION_TYPE, (verificationType ?: VerificationType.FORGOT_PASSWORD).name)
            intent.putExtra(Constant.MOBILE_NUMBER, mobileNumber)
            startActivity(intent)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            verifyBtn.setOnClickListener {
                mobileNumber = inputMobileNumber.text.toString()

                validateFormData(mobileNumber)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    when(verificationType) {
                        VerificationType.CHANGE_MOBILE_NUMBER -> viewModel.requestUpdateMobileOtp(mobileNumber)
                        else -> viewModel.requestForgotPassword(mobileNumber)
                    }
                }
            }
        }
    }

    private fun validateFormData(mobileNumber: String): String? {
        return if (!UtilityActions.isValidMobileNumber(mobileNumber)) getString(R.string.enter_valid_mobile_number)
        else null
    }
}