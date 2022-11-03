package com.aajeevika.individual.view.suvidha.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.SuvidhaData
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class SuvidhaViewModel : BaseViewModel() {
    private val _suvidhaListResponse = MutableLiveData<ArrayList<SuvidhaData>>()
    val suvidhaListResponse: LiveData<ArrayList<SuvidhaData>> = _suvidhaListResponse

    fun getSuvidhaList() {
        requestData(
            { apiService.getSuvidhaList() },
            { it.data?.list?.run { _suvidhaListResponse.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}