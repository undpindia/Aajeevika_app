package com.aajeevika.individual.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.OtpModel
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RegisterViewModel : BaseViewModel() {
    private val _otpLiveData = MutableLiveData<OtpModel>()
    val otpLiveData: LiveData<OtpModel> = _otpLiveData

    fun requestUserRegistration(profile: File, name: String, email: String, mobileNumber: String, password: String) {
        val requestMap = HashMap<String, RequestBody>()
        requestMap["role_id"] = Constant.USER_ROLE_ID.toString().toRequestBody()
        requestMap["name"] = name.toRequestBody()
        requestMap["email"] = email.toRequestBody()
        requestMap["mobile"] = mobileNumber.toRequestBody()
        requestMap["password"] = password.toRequestBody()

        requestData(
            { apiService.registerUser(requestMap, UtilityActions.multipartImage(profile, "profileimage")) },
            { it.data?.run { _otpLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }
}