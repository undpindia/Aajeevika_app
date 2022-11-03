package com.aajeevika.individual.model.data_model

import java.io.Serializable

data class ProductListDataModel(
    val catProduct: ArrayList<CatProductData>,
    val pagination: PaginationData,
)

data class CatProductData(
    val id: Int,
    val name: String,
    val ind_products: ArrayList<IndProductData>,
)

data class IndProductData(
    val id: Int,
    val name: String,
    val image: String,
    val price: String,
    val price_unit: String,
    var quantity: Int = 0,
) : Serializable

data class PaginationData(
    val current_page: Int,
    val last_page: Int,
)