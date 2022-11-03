package com.aajeevika.individual.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.HomePageDataModel
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class DashboardViewModel : BaseViewModel() {
    private val _homeDataLiveData = MutableLiveData<HomePageDataModel>()
    val homeDataLiveData: LiveData<HomePageDataModel> = _homeDataLiveData

    fun getHomePage(user_id: Int) {
        val requestMap = hashMapOf<String, Any>(Pair("user_id", user_id))

        requestData(
            { apiService.getHomeData(requestMap) },
            { it.data?.run { _homeDataLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}