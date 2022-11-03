package com.aajeevika.individual.view.documents

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.autofill.AutofillManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityDocumentsBinding
import com.aajeevika.individual.location.activity.MapsActivity
import com.aajeevika.individual.location.constants.*
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.utility.UtilityActions.showMessage
import com.aajeevika.individual.utility.app_enum.DocumentType
import com.aajeevika.individual.view.auth.ActivityLogin
import com.aajeevika.individual.view.dialog.AlertDialog
import com.aajeevika.individual.view.dialog.MediaDialog
import com.aajeevika.individual.view.documents.viewmodel.DocumentsViewModel
import com.aajeevika.individual.view.home.ActivityDashboard
import com.aajeevika.individual.view.profile.ActivitySelectInterest
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
    private lateinit var aadharBackImage: File
    private lateinit var aadharFrontImage: File
    private val files = ArrayList<MultipartBody.Part>()
    private val fieldMap = HashMap<String, RequestBody>()

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

    private val uCropCallBack = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it?.data?.let { intent ->
            UCrop.getOutput(intent)?.toFile()?.let { file ->
                when (file.name) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(AutofillManager::class.java).disableAutofillServices()
        }

        documentType?.let {
            viewDataBinding.run {
                aadharCard.isRequired = true
                aadharCard.root.visibility = View.VISIBLE
                registeredAddress.root.visibility = View.GONE
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
                if(documentType == null) {
                    val intent = Intent(this@ActivityDocuments, ActivitySelectInterest::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    val intent = Intent(this@ActivityDocuments, ActivityDashboard::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
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