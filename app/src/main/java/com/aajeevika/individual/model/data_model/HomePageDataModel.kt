package com.aajeevika.individual.model.data_model

data class HomePageDataModel(
    val matchingUser: ArrayList<MatchingSellerData>,
    val favUser: ArrayList<FavSellerData>,
)

data class MatchingSellerData(
    val id: Int,
    val name: String,
    val organization_name: String?,
    val profileImage: String?,
)

data class FavSellerData(
    val id: Int,
    val name: String,
    val organization_name: String?,
    val profileImage: String?,
)