package com.aajeevika.individual.location

import com.aajeevika.individual.location.model.PlaceDetailsFromLatLngModel
import com.aajeevika.individual.location.model.PlaceDetailsResponse
import com.aajeevika.individual.location.model.SearchLocationResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MapApiService {
    @GET("/maps/api/geocode/json")
    suspend fun getAddressFromLatLng(
        @QueryMap path: HashMap<String, String>
    ): PlaceDetailsFromLatLngModel

    @GET("maps/api/place/autocomplete/json")
    suspend fun getLocation(
        @QueryMap map: HashMap<String, String>
    ): SearchLocationResponse

    @GET("maps/api/place/details/json")
    suspend fun getPlaceDetails(
        @QueryMap map: HashMap<String, String>
    ): PlaceDetailsResponse
}