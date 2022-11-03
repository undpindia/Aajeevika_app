package com.aajeevika.individual.view.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.individual.baseclasses.BaseViewModel
import com.aajeevika.individual.model.data_model.ChatMessageDataModel
import com.aajeevika.individual.model.data_model.MessageModel
import com.aajeevika.individual.model.network_request.ChatApiService
import com.aajeevika.individual.utility.app_enum.ErrorType
import com.aajeevika.individual.utility.app_enum.ProgressAction
import org.koin.java.KoinJavaComponent

class ChatViewModel : BaseViewModel() {
    private val apiChatService: ChatApiService by KoinJavaComponent.inject(clazz = ChatApiService::class.java)

    private val _chatListOldLiveData = MutableLiveData<ArrayList<MessageModel>>()
    val chatListOldLiveData: LiveData<ArrayList<MessageModel>> = _chatListOldLiveData

    private val _suggestionLiveData = MutableLiveData<ArrayList<ChatMessageDataModel>>()
    val suggestionLiveData: LiveData<ArrayList<ChatMessageDataModel>> = _suggestionLiveData

    fun getChatList() {
        /*requestData(
            {
                apiService.getGetChatMessage()
            },
            {
                it.data?.run {
                    _chatListLiveData.postValue(chat_message_list)
                }
            },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )*/
    }

    fun getSuggestionList() {
        requestData(
            {
                apiService.getSuggestionMessage()
            },
            {
                it.data?.run {
                    _suggestionLiveData.postValue(chat_message_list)
                }
            },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }

    fun startChatConversation(userId: String, receiverUserId: String) {
        val map = java.util.HashMap<String, String>()
        map["toUserId"] = userId
        map["fromUserId"] = receiverUserId
        map["aliasId"] = "1"
        map["orderId"] = "1"
        map["libraryId"] = "1"
        map["byUserWishListId"] = "1"
        map["userWishListId"] = "1"
        requestPlaceData(
            {
                apiChatService.startChatConversation(map)
            },
            {

            },
            errorType = ErrorType.NONE,
        )

    }

    fun getAllMessage(userId: String, receiverUserId: String) {
        val map = java.util.HashMap<String, String>()
        map["toUserId"] = receiverUserId
        map["userId"] = userId
        map["aliasId"] = "1"
        requestPlaceData(
            {
                apiChatService.getMessages(map)
            },
            {
                if (it.error) {

                } else {
                    _chatListOldLiveData.postValue(it.messages)
                }
            },
            errorType = ErrorType.MESSAGE,
        )

    }

}