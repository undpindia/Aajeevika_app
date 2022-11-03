package com.aajeevika.individual.view.survey.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.SurveyData
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class SurveyViewModel: BaseViewModel() {
    private val _surveyListLiveData = MutableLiveData<ArrayList<SurveyData>>()
    val surveyListLiveData: LiveData<ArrayList<SurveyData>> = _surveyListLiveData

    fun getSurveyList() {
        requestData(
            { apiService.getSurveyList() },
            { it.data?.survey_list?.run { _surveyListLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}