package com.aajeevika.individual.model.data_model

data class MessageListResponse(
    val error: Boolean,
    val messages: ArrayList<MessageModel>
)

data class ImageUpload(
    val statusCode: Int,
    val message: String,
    val status: Boolean,
    val result: String
)

data class MessageModel(
    val _id: String = "",
    val fromUserId: String = "",
    val toUserId: String = "",
    val userId: String = "",
    val message: String = "",
    val messagehi: String = "",
    val messageen: String = "",
    val type: String = "",
    val isRead: Int = 0,
    var file: String = "",
    var timestamp: String = "0"
)

data class ChatUserResponse(
    val userList: ArrayList<UsersResult>? = ArrayList(),
    val error: Boolean
)

data class UsersResult(
    var _id: Any,
    var aliasId: String = "0",
    var fromUserId: String = "",
    var toUserId: String = "",
    var receiverStatus: String = "",
    var updatedAt: String = "",
    var messagesData: MessageData?,
    val receiverImage: String = "",
    val receiverName: String = "",
    val aliasUserName: String = "",
    val receiverId: String = "",
    val libraryId: String = "",
    val byUserWishListId: String = "",
    val userWishListId: String = "",
    val orderId: String = ""
)