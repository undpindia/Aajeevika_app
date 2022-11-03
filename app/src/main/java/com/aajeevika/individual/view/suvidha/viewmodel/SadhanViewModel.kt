package com.aajeevika.individual.view.suvidha.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.SadhanData
import com.aajeevika.individual.model.data_model.SuvidhaData
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class SadhanViewModel : BaseViewModel() {
    private val _sadhanListResponse = MutableLiveData<ArrayList<SadhanData>>()
    val sadhanListResponse: LiveData<ArrayList<SadhanData>> = _sadhanListResponse

    fun getSadhanList() {
        requestData(
            { apiService.getSadhanList() },
            { it.data?.sadhan_list?.run { _sadhanListResponse.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}