package com.aajeevika.individual.model.data_model

data class ClfListDataModel(
    val CLF_list: ArrayList<ClfData>
)

data class ClfData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val organization_name: String,
)