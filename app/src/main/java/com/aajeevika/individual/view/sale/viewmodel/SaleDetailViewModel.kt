package com.aajeevika.individual.view.sale.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.BuyDetailDataModel
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class SaleDetailViewModel : BaseViewModel() {
    private val _saleDetailLiveData = MutableLiveData<BuyDetailDataModel>()
    val saleDetailLiveData: LiveData<BuyDetailDataModel> = _saleDetailLiveData

    fun getIndividualSaleDetails(id: Int) {
        val requestMap = hashMapOf<String, Any>(Pair("order_id", id))

        requestData(
            { apiService.getIndividualSaleDetails(requestMap) },
            { it.data?.run { _saleDetailLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}