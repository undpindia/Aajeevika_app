package com.aajeevika.individual.view.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.RatingDataModel
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class RatingAndReviewsViewModel : BaseViewModel() {
    private val _reviewLiveData = MutableLiveData<RatingDataModel>()
    val reviewLiveData: LiveData<RatingDataModel> = _reviewLiveData

    fun getReviews(userId: Int, page: Int = 1) {
        val requestMap = HashMap<String, Any>()
        requestMap["user_id"] = userId
        requestMap["page"] = page

        requestData(
            { apiService.getReviews(requestMap) },
            { it.data?.run { _reviewLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}