package com.aajeevika.individual.model.data_model

data class ChatMessageResponseModel(
    val chat_message_list: ArrayList<ChatMessageDataModel>,
)

data class ChatMessageDataModel(
    val id: Int,
    val msg_en: String?,
    val msg_hi: String?,
    val status: Int
)