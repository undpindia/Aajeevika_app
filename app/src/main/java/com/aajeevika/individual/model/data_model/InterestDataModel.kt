package com.aajeevika.individual.model.data_model

data class InterestDataModel(
    val interestList: ArrayList<InterestData>
)

data class InterestData(
    val id: Int,
    val name: String,
    val image: String,
)
