package com.aajeevika.individual.model.data_model

data class SaleListDataModel(
    val order_list: ArrayList<SaleData>
)

data class SaleData(
    val id: Int,
    val order_id_d: String,
    val created_at: String,
    val get_clf: SaleClfData,
)

data class SaleClfData(
    val organization_name: String
)