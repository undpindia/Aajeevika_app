package com.aajeevika.individual.model.data_model

data class GrievanceChatDataModel(
    val ticket_chat_list: ArrayList<ChatData>
)

data class ChatData(
    val message: String,
    val status: Int,
    val get_issue: IssueData,
    val get_message: ArrayList<MessageData>,
)

data class IssueData(
    val name: String,
)

data class MessageData(
    val _id: String,
    val toUserId: String,
    val fromUserId: String,
    val message: String?,
    val messagehi: String?,
    val messageen: String?,
    val aliasId: String,
    val timestamp: String,
)
