package com.aajeevika.individual.view.profile

import BaseUrls
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityProfileBinding
import com.aajeevika.individual.model.data_model.AddressData
import com.aajeevika.individual.model.data_model.UserData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.utility.app_enum.DocumentType
import com.aajeevika.individual.view.dialog.ImagePreviewDialog
import com.aajeevika.individual.view.dialog.MediaDialog
import com.aajeevika.individual.view.documents.ActivityDocuments
import com.aajeevika.individual.view.profile.adapter.UserInterestRecyclerViewAdapter
import com.aajeevika.individual.view.profile.viewmodel.ProfileViewModel
import com.yalantis.ucrop.UCrop
import java.io.File
import java.lang.StringBuilder

class ActivityProfile : BaseActivityVM<ActivityProfileBinding, ProfileViewModel>(
    R.layout.activity_profile,
    ProfileViewModel::class
) {
    private val userInterestRecyclerViewAdapter = UserInterestRecyclerViewAdapter(preferencesHelper)
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

        viewDataBinding.recyclerView.run {
            adapter = userInterestRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(16F,16F,16F,16F))
        }

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
                preferencesHelper.email = email ?: ""
                preferencesHelper.mobile = mobile ?: ""
                preferencesHelper.profileImage = profileImage ?: ""
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
                isAadharAdded = it.user?.is_aadhar_added == 1
                executePendingBindings()
            }

            userInterestRecyclerViewAdapter.addData(it.individualInterest)
        })

        viewModel.statusLiveData.observe(this, {
            viewModel.requestUserProfile()
        })
    }

    private fun updateUserProfile() {
        viewDataBinding.run {
            mobileNumber = preferencesHelper.mobile
            name = preferencesHelper.name
            emailId = preferencesHelper.email
            address = preferencesHelper.address

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
                intent.putExtra(Constant.USER_ID, preferencesHelper.uid)
                startActivity(intent)
            }

            personalDetailsBtn.setOnClickListener {
                personalDetailsContainer.visibility = if(personalDetailsContainer.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }

            additionalDetailsBtn.setOnClickListener {
                additionalDetailsConatiner.visibility = if(additionalDetailsConatiner.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }

            docTypeAadhar.run {
                verificationStatus.setOnClickListener {
                    if(isAadharAdded && !isAadharVerified) uploadDocument()
                }

                documentBtn.setOnClickListener {
                    userData.adhar_card_front_file?.let { frontFile ->
                        userData.adhar_card_back_file?.let { backFile ->
                            ImagePreviewDialog(this@ActivityProfile, BaseUrls.baseUrl + frontFile, BaseUrls.baseUrl + backFile).show()
                        }
                    }
                }

                uploadDocumentBtn.setOnClickListener {
                    uploadDocument()
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

            editInterestBtn.setOnClickListener {
                val intent = Intent(this@ActivityProfile, ActivitySelectInterest::class.java)
                intent.putExtra(Constant.INTEREST_LIST, userInterestRecyclerViewAdapter.getData())
                intent.putExtra(Constant.EDIT, true)
                startActivity(intent)
            }
        }
    }

    private fun uploadDocument() {
        val intent = Intent(this@ActivityProfile, ActivityDocuments::class.java)
        intent.putExtra(Constant.USER_ROLE, preferencesHelper.roleId)
        intent.putExtra(Constant.DOCUMENT_TYPE, DocumentType.AADHAR.name)
        startActivity(intent)
    }
}