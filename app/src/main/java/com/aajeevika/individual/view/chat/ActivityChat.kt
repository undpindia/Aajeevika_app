package com.aajeevika.individual.view.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivitySocket
import com.aajeevika.individual.databinding.ActivityChatBinding
import com.aajeevika.individual.model.data_model.MessageModel
import com.aajeevika.individual.model.data_model.UsersResult
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.view.chat.adapter.MyChatRecyclerViewAdapter
import com.aajeevika.individual.view.chat.viewmodel.ChatViewModel

class ActivityChat : BaseActivitySocket<ActivityChatBinding, ChatViewModel>(
    R.layout.activity_chat,
    ChatViewModel::class
) {
    private val myChatRecyclerViewAdapter by lazy {
        MyChatRecyclerViewAdapter(
            preferencesHelper.uid.toString(),
            preferencesHelper.appLanguage
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.run {
            adapter = myChatRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(12F, 12F, 12F, 12F))
        }
    }


    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getChatList()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mSocket?.on("chat-list-response", onUserChatList)
    }

    override fun onConnect() {
        super.onConnect()
        Handler(Looper.getMainLooper()).postDelayed({
            getUserList()
        }, 100)
    }

    override fun onNewMessage(msg: MessageModel) {
        getUserList()
    }

    override fun onUserChatList(list: ArrayList<UsersResult>) {
        super.onUserChatList(list)
        //mViewModel.progress.set(false)
//        userList = list
//        chatListAdapter?.updateList(userList)

        myChatRecyclerViewAdapter.addData(list)
        refreshPage()
    }

    private fun refreshPage() {
        //mViewModel.message.set(if (userList.isEmpty()) getString(R.string.empty_no_chat_found) else "")
    }

}