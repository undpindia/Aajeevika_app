package com.aajeevika.individual.view.other.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.FaqData
import com.aajeevika.individual.model.data_model.TicketData
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class FaqViewModel : BaseViewModel() {
    private val _allFaq = MutableLiveData<ArrayList<FaqData>>()
    val faq: LiveData<ArrayList<FaqData>> = _allFaq

    fun getAllFaq(){
        requestData(
            { apiService.getFaq() },
            { it.data?.list?.run { _allFaq.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}