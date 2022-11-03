package com.aajeevika.individual.view.profile

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityEditProfileBinding
import com.aajeevika.individual.model.data_model.AddressData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions.showMessage
import com.aajeevika.individual.utility.app_enum.VerificationType
import com.aajeevika.individual.view.auth.ActivityEnterMobile
import com.aajeevika.individual.view.dialog.AlertDialog
import com.aajeevika.individual.view.profile.viewmodel.ProfileViewModel

class ActivityEditProfile : BaseActivityVM<ActivityEditProfileBinding, ProfileViewModel>(
    R.layout.activity_edit_profile,
    ProfileViewModel::class
) {
    private val addressData by lazy { intent.getSerializableExtra(Constant.ADDRESS) as? AddressData }

    private val editAddressCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) finish()
    }

    override fun onStart() {
        super.onStart()

        viewDataBinding.run {
            inputName.setText(preferencesHelper.name)
            inputEmail.setText(preferencesHelper.email)
            inputMobileNumber.setText(preferencesHelper.mobile)
            inputAddress.setText(preferencesHelper.address)
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
                val name = inputName.text.toString().trim()
                val email = inputEmail.text.toString().trim()

                validateFormData(name, email)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    viewModel.updateProfileData(name, email)
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

    private fun validateFormData(name: String, email: String) : String? {
        return if(name.isEmpty() || email.isEmpty()) getString(R.string.fill_all_the_fields)
        else null
    }
}