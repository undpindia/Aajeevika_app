package com.aajeevika.individual.view.sale.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.SaleData
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class SalesViewModel : BaseViewModel() {
    private val _salesLiveData = MutableLiveData<ArrayList<SaleData>>()
    val salesLiveData: LiveData<ArrayList<SaleData>> = _salesLiveData

    fun getSalesList(page: Int = 1) {
        val requestMap = HashMap<String, Any>()
        requestMap["page"] = page

        requestData(
            { apiService.getSalesList(requestMap) },
            { it.data?.run { _salesLiveData.postValue(order_list) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}