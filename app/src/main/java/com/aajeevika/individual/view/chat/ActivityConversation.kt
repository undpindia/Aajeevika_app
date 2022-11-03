package com.aajeevika.individual.view.chat

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivitySocket
import com.aajeevika.individual.databinding.ActivityConversationBinding
import com.aajeevika.individual.databinding.ListItemChipBinding
import com.aajeevika.individual.model.data_model.ChatMessageDataModel
import com.aajeevika.individual.model.data_model.MessageModel
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.app_enum.LanguageType
import com.aajeevika.individual.view.chat.adapter.ConversionRecyclerViewAdapter
import com.aajeevika.individual.view.chat.viewmodel.ChatViewModel
import io.socket.emitter.Emitter
import org.json.JSONObject

class ActivityConversation : BaseActivitySocket<ActivityConversationBinding, ChatViewModel>(
    R.layout.activity_conversation,
    ChatViewModel::class
) {
    private val pageTitle by lazy {
        intent.getStringExtra(Constant.TITLE) ?: getString(R.string.my_chat)
    }
    private val individualId by lazy { intent.getStringExtra(Constant.USER_ID) ?: "" }
    private val conversionRecyclerViewAdapter by lazy {
        ConversionRecyclerViewAdapter(
            preferencesHelper.uid.toString(),
            preferencesHelper.appLanguage
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.run {
            title = pageTitle
            recyclerView.adapter = conversionRecyclerViewAdapter
        }
        viewModel.getSuggestionList()
        //viewModel.getAllMessage(preferencesHelper.uid.toString(), individualId.toString())
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
            shareNumberBtn.setOnClickListener {
                val mobile = preferencesHelper.mobile
                emitMessage(mobile, mobile, mobile)
            }
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.suggestionLiveData.observe(this, Observer {
            viewDataBinding.chipGroup.removeAllViews()
            it.forEach { chip ->
                val viewBinding =
                    ListItemChipBinding.inflate(layoutInflater, viewDataBinding.chipGroup, false)
                viewBinding.title = when (preferencesHelper.appLanguage) {
                    LanguageType.HINDI.code -> chip.msg_hi ?: chip.msg_en ?: ""
                    else -> chip.msg_en ?: chip.msg_hi ?: ""
                }
                viewBinding.actionChip.setOnClickListener {
                    sendMessage(chip)
                }
                viewDataBinding.chipGroup.addView(viewBinding.root)
            }
        })
        viewModel.chatListOldLiveData.observe(this, Observer {
            conversionRecyclerViewAdapter?.let { adapter ->
                adapter.addData(it)
                viewDataBinding.recyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        })
    }

    private fun sendMessage(message: ChatMessageDataModel) {
        val msg = when (preferencesHelper.appLanguage) {
            LanguageType.HINDI.code -> message.msg_hi
            else -> message.msg_en
        } ?: ""
        emitMessage(msg, message.msg_hi ?: "", message.msg_en ?: "")
    }

    /*-----------CHAT ------------------*/
    override fun onConnect() {
        readMessage()
    }

    override fun onStart() {
        super.onStart()
        mSocket?.on("isRead-response", onReadMessage)
        //  getAllChatFromServer()
        viewModel.startChatConversation(preferencesHelper.uid.toString(), individualId.toString())
        viewModel.getAllMessage(preferencesHelper.uid.toString(), individualId.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        destroySocket()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        destroySocket()
    }

    private fun emitMessage(msg: String, msgHi: String, msgEn: String) {

        val data1 = JSONObject()
        data1.put("toUserId", individualId.toString())
        data1.put("fromUserId", preferencesHelper.uid.toString())
        data1.put("type", "text")
        data1.put("isRead", "0")
        data1.put("file ", "")
        data1.put("message", msg)
        data1.put("messagehi", msgHi)
        data1.put("messageen", msgEn)

        data1.put("libraryId", "1")
        data1.put("receiverUserId", preferencesHelper.uid.toString())
        data1.put("aliasId", "1")
        data1.put("userWishListId", "1")
        data1.put("byUserWishListId", "1")

/*
            if (mViewModel.data.value?.order?.user_id == mPrefs.userId) {
                data1.put("userWishListId", mViewModel.data.value?.order?.by_user_wishlist_id ?: "")
                data1.put("byUserWishListId", mViewModel.data.value?.order?.wishlist_id ?: "")
            } else {
                data1.put("userWishListId", mViewModel.data.value?.order?.wishlist_id ?: "")
                data1.put(
                    "byUserWishListId",
                    mViewModel.data.value?.order?.by_user_wishlist_id ?: ""
                )
            }
*/

        mSocket?.emit("add-message", data1)
        Log.d(TAG_SOCKET, "${individualId} emitMessage " + data1)
        val messageModel = MessageModel(
            fromUserId = preferencesHelper.uid.toString(),
            toUserId = individualId.toString(),
            type = "text",
            file = "",
            isRead = 0,
            message = msg,
            messageen = msgEn,
            messagehi = msgHi,
            timestamp = System.currentTimeMillis().toString()
        )
        addMessageToList(messageModel)
        //etMessage.setText("")
    }

/*
    private fun emitMessageImage(url: String) {
        val data1 = JSONObject()
        data1.put("toUserId", mViewModel.mReceiverUserID)
        data1.put("fromUserId", mPrefs.userId)
        data1.put("type", "image")
        data1.put("isRead", "0")
        data1.put("file ", "")
        data1.put("message", url)

        data1.put("libraryId", mViewModel.mLibraryID)
        data1.put("receiverUserId", mPrefs.userId)

        data1.put("aliasId", mViewModel.mAliasID)

        if (mViewModel.data.value?.order?.user_id == mPrefs.userId) {
            data1.put("userWishListId", mViewModel.data.value?.order?.by_user_wishlist_id ?: "")
            data1.put("byUserWishListId", mViewModel.data.value?.order?.wishlist_id ?: "")
        } else {
            data1.put("userWishListId", mViewModel.data.value?.order?.wishlist_id ?: "")
            data1.put(
                "byUserWishListId",
                mViewModel.data.value?.order?.by_user_wishlist_id ?: ""
            )
        }

        mSocket?.emit("add-message", data1)
        Log.d(TAG_SOCKET, "${mViewModel.mReceiverUserID} emitMessage " + data1)

        val messageModel = MessageModel(
            fromUserId = mPrefs.userId,
            toUserId = mViewModel.mReceiverUserID,
            type = "image",
            file = "",
            isRead = 0,
            message = url,
            timestamp = System.currentTimeMillis().toString()
        )
        addMessageToList(messageModel)
    }
*/

    private fun readMessage() {
        val data1 = JSONObject()
        data1.put("toUserId", individualId)
        data1.put("fromUserId", preferencesHelper.uid.toString())
        data1.put("aliasId", "1")
        mSocket?.emit("isRead", data1)
        Log.d(TAG_SOCKET, "${individualId} readMessage " + data1)
    }

    private fun addMessageToList(messageModel: MessageModel) {
        conversionRecyclerViewAdapter?.let { adapter ->
            adapter.add(messageModel)
            viewDataBinding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private val onReadMessage = Emitter.Listener { args ->
        Log.d(TAG_SOCKET, "onReadMessage call")
        this@ActivityConversation.runOnUiThread {
            val json = args[0] as JSONObject
            Log.d("dataResponse", "ReadMessage " + json)
        }
    }

    override fun onNewMessage(msg: MessageModel) {
        if (individualId.toString() == msg.fromUserId) {
            addMessageToList(msg)
            readMessage()
        }
    }
}