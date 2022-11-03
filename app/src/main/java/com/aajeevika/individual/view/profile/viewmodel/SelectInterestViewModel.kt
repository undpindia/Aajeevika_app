package com.aajeevika.individual.view.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.InterestData
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class SelectInterestViewModel : BaseViewModel() {
    private val _interestList = MutableLiveData<ArrayList<InterestData>>()
    val interestList: LiveData<ArrayList<InterestData>> = _interestList

    private val _requestStatusLiveData = MutableLiveData<Boolean>()
    val requestStatusLiveData: LiveData<Boolean> = _requestStatusLiveData

    fun getInterestList() {
        requestData(
            { apiService.getInterestList() },
            { it.data?.run { _interestList.postValue(interestList) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }

    fun addInterestList(userId: Int, interestIds: String) {
        val requestMap = hashMapOf<String, Any>(
            Pair("interest_list_id", interestIds),
            Pair("user_id", userId),
        )

        requestData(
            { apiService.addInterestList(requestMap) },
            { _requestStatusLiveData.postValue(it.status) },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}