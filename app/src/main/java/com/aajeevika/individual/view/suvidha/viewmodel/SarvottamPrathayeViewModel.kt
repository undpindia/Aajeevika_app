package com.aajeevika.individual.view.suvidha.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.SarvottamPrathayeData
import com.aajeevika.individual.model.data_model.SuvidhaData
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class SarvottamPrathayeViewModel : BaseViewModel() {
    private val _sarvottamPrathayeListResponse = MutableLiveData<ArrayList<SarvottamPrathayeData>>()
    val sarvottamPrathayeListResponse: LiveData<ArrayList<SarvottamPrathayeData>> = _sarvottamPrathayeListResponse

    fun getSuvidhaList() {
        requestData(
            { apiService.getSarvottamPrathayeList() },
            { it.data?.list?.run { _sarvottamPrathayeListResponse.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}