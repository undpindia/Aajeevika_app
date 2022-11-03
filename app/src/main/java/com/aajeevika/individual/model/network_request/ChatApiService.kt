package com.aajeevika.individual.model.network_request

import com.aajeevika.individual.model.data_model.ImageUpload
import com.aajeevika.individual.model.data_model.MessageListResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ChatApiService {
    @POST("shopmate/v1/startChatConversation")
    suspend fun startChatConversation(@Body map: HashMap<String, String>): String

    @POST("shopmate/v1/getMessages")
    suspend fun getMessages(@Body map: HashMap<String, String>): MessageListResponse

    @Multipart
    @POST("shopmate/v1/imageUpload")
    suspend fun mediaUpload(@Part file: MultipartBody.Part): ImageUpload
}