package com.aajeevika.individual.view.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.BlockData
import com.aajeevika.individual.model.data_model.CityData
import com.aajeevika.individual.model.data_model.UserProfileModel
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction
import okhttp3.MultipartBody

class IndividualProfileViewModel : BaseViewModel() {
    private val _userLiveData = MutableLiveData<UserProfileModel>()
    val userLiveData: LiveData<UserProfileModel> = _userLiveData

    private val _requestStatusLiveData = MutableLiveData<Boolean>()
    val requestStatusLiveData: LiveData<Boolean> = _requestStatusLiveData

    fun getIndividualProfile(id: Int) {
        val requestMap = hashMapOf<String, Any>(Pair("individual_id", id))

        requestData(
            { apiService.getIndividualProfile(requestMap) },
            { it.data?.run { _userLiveData.postValue(this) }},
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }

    fun addFavorite(id: Int, status: Int) {
        val requestMap = hashMapOf<String, Any>(
            Pair("seller_id", id),
            Pair("status", status),
        )

        requestData(
            { apiService.addFavorite(requestMap) },
            { _requestStatusLiveData.postValue(it.status) },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}