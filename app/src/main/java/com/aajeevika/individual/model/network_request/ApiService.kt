package com.aajeevika.individual.model.network_request

import com.aajeevika.individual.model.data_model.BaseDataModel
import com.aajeevika.individual.model.data_model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/get_countries")
    suspend fun getCountries(): Response<BaseDataModel<CountryModel>>

    @FormUrlEncoded
    @POST("api/get_state")
    suspend fun getState(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<StateModel>>

    @FormUrlEncoded
    @POST("api/get_city")
    suspend fun getCity(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<CityModel>>

    @FormUrlEncoded
    @POST("api/get-block")
    suspend fun getBlock(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<BlockModel>>

    @Multipart
    @POST("api/registration")
    suspend fun registerUser(
        @PartMap fields: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part,
    ): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/login")
    suspend fun loginUser(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/verifyotp")
    suspend fun verifyOtp(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/updatemobile")
    suspend fun updateMobileNumber(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/resendotp")
    suspend fun resendOtp(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/forgetpassword")
    suspend fun forgotPassword(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/changepassword")
    suspend fun changePassword(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/getnotification")
    suspend fun getNotification(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<NotificationDataModel>>

    @GET("api/getprofile")
    suspend fun getProfile(): Response<BaseDataModel<UserProfileModel>>

    @GET("api/get-survey-list")
    suspend fun getSurveyList(): Response<BaseDataModel<SurveyDataModel>>

    @FormUrlEncoded
    @POST("api/ind-get-details")
    suspend fun getIndividualProfile(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/updateuserprofile")
    suspend fun updateUserProfile(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/updateaddress")
    suspend fun updateAddress(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @Multipart
    @POST("api/updateprofileimage")
    suspend fun uploadProfileImage(@Part file: MultipartBody.Part): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/ind-get-review")
    suspend fun getReviews(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<RatingDataModel>>

    @FormUrlEncoded
    @POST("api/interest-list")
    suspend fun getInterestList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<InterestDataModel>>

    @FormUrlEncoded
    @POST("api/add-interest-list")
    suspend fun addInterestList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/individual-home")
    suspend fun getHomeData(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<HomePageDataModel>>

    @FormUrlEncoded
    @POST("api/ind-order-list")
    suspend fun getSalesList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<SaleListDataModel>>

    @FormUrlEncoded
    @POST("api/ind-get-clf-list")
    suspend fun getClfList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<ClfListDataModel>>

    @POST("api/ind-add-order")
    suspend fun addNewOrder(@Body data: JsonObject): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/ind-add-product")
    suspend fun addNewProductList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<ProductListDataModel>>

    @FormUrlEncoded
    @POST("api/add-favorite")
    suspend fun addFavorite(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/get-clf-ind-order-details")
    suspend fun getIndividualSaleDetails(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<BuyDetailDataModel>>

    @FormUrlEncoded
    @POST("api/get-grievance-type-list")
    suspend fun getGrievanceTypeList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<GrievanceTypeDataModel>>

    @FormUrlEncoded
    @POST("api/add-grievance-ticket")
    suspend fun addGrievanceTicket(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/get-ticket-list")
    suspend fun getTicketList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<TicketsDataModel>>

    @FormUrlEncoded
    @POST("api/get-ticket-chat-list")
    suspend fun getTicketChatList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<GrievanceChatDataModel>>

    @FormUrlEncoded
    @POST("api/add-grievance-message")
    suspend fun sendMessage(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/suvidha-list")
    suspend fun getSuvidhaList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<SuvidhaListData>>

    @FormUrlEncoded
    @POST("api/sarvottam-list")
    suspend fun getSarvottamPrathayeList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<SarvottamPrathayeListData>>

    @FormUrlEncoded
    @POST("api/sadhan-list")
    suspend fun getSadhanList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<SadhanListData>>

    @Multipart
    @POST("api/adddocumentandaddress")
    suspend fun addDocumentAndAddress(
        @PartMap fields: HashMap<String, RequestBody>,
        @Part files: ArrayList<MultipartBody.Part> = ArrayList()
    ): Response<BaseDataModel<UserProfileModel>>

    @Multipart
    @POST("api/updatedocument")
    suspend fun reUploadDocument(
        @PartMap fields: HashMap<String, RequestBody>,
        @Part files: ArrayList<MultipartBody.Part> = ArrayList()
    ): Response<BaseDataModel<UserProfileModel>>

    @POST("/api/faq-topic-list")
    suspend fun getFaq(): Response<BaseDataModel<FaqModel>>

    @GET("/api/get-chat-messages")
    suspend fun getSuggestionMessage(): Response<BaseDataModel<ChatMessageResponseModel>>

}