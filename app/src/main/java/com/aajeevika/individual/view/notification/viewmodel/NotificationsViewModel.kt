package com.aajeevika.individual.view.notification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.NotificationDataModel
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class NotificationsViewModel : BaseViewModel() {
    private val _notificationLiveData = MutableLiveData<NotificationDataModel>()
    val notificationLiveData: LiveData<NotificationDataModel> = _notificationLiveData

    fun getNotificationList(page: Int = 1) {
        val requestMap = HashMap<String, Any>()
        requestMap["page"] = page

        requestData(
            { apiService.getNotification(requestMap) },
            { it.data?.run { _notificationLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}