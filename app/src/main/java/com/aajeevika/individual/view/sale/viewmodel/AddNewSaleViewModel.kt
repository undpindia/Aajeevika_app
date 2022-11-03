package com.aajeevika.individual.view.sale.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.ClfData
import com.aajeevika.individual.model.data_model.IndProductData
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class AddNewSaleViewModel : BaseViewModel() {
    private val _clfListLiveData = MutableLiveData<ArrayList<ClfData>>()
    val clfListLiveData: LiveData<ArrayList<ClfData>> = _clfListLiveData

    private val _saleStatusLiveData = MutableLiveData<String>()
    val saleStatusLiveData: LiveData<String> = _saleStatusLiveData

    fun getClfList() {
        requestData(
            { apiService.getClfList() },
            { it.data?.CLF_list?.run { _clfListLiveData.postValue(this) }},
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.TOAST,
        )
    }

    fun addNewSale(userId: Int, rating: Float, review: String, saleDate: String, productList: ArrayList<IndProductData>) {
        val products = JsonArray()
        productList.forEach {
            val jsonObject = JsonObject()
            jsonObject.addProperty("product_id", it.id)
            jsonObject.addProperty("quantity", it.quantity)
            products.add(jsonObject)
        }

        val requestBody = JsonObject()
        requestBody.addProperty("user_id", userId)
        requestBody.addProperty("rating", rating)
        requestBody.addProperty("review_msg", review)
        requestBody.addProperty("type", "seller")
        requestBody.addProperty("sale_date", saleDate)
        requestBody.add("products", products)

        requestData(
            { apiService.addNewOrder(requestBody) },
            { it.message?.run { _saleStatusLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}