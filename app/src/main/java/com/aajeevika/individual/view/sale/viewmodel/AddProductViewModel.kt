package com.aajeevika.individual.view.sale.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.ProductListDataModel
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction

class AddProductViewModel : BaseViewModel() {
    private val _productLiveData = MutableLiveData<ProductListDataModel>()
    val productLiveData: LiveData<ProductListDataModel> = _productLiveData

    fun addNewProductList(page: Int = 1, search: String = "") {
        val requestMap = HashMap<String, Any>()
        requestMap["page"] = page
        requestMap["search"] = search

        requestData(
            { apiService.addNewProductList(requestMap) },
            { it.data?.run { _productLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}