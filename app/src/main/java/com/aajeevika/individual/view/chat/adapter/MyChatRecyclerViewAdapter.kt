package com.aajeevika.individual.view.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemChatBinding
import com.aajeevika.individual.model.data_model.MessageData
import com.aajeevika.individual.model.data_model.UsersResult
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.app_enum.LanguageType
import com.aajeevika.individual.utility.extension.setFormatTime
import com.aajeevika.individual.view.chat.ActivityConversation

class MyChatRecyclerViewAdapter(private val mUserId: String, private val language: String) :
    BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<UsersResult>()
    fun addData(data: ArrayList<UsersResult>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return ListItemChatViewHolder(ListItemChatBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ListItemChatViewHolder(val dataBinding: ListItemChatBinding) :
        BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]
            dataBinding.run {
                profileImage = data.receiverImage
                userName = data.receiverName
                message = getMessage(data.messagesData)
                date = data.messagesData?.let { it.timestamp.setFormatTime("YYYY/MM/DD") } ?: ""
                root.setOnClickListener {
                    val intent = Intent(context, ActivityConversation::class.java)
                    intent.putExtra(
                        Constant.USER_ID,
                        if (mUserId.equals(
                                data.toUserId,
                                true
                            )
                        ) data.fromUserId else data.toUserId
                    )
                    context.startActivity(intent)
                }
            }
        }
    }

    private fun getMessage(message: MessageData?): String {
        return when (language) {
            LanguageType.HINDI.code -> {
                message?.messagehi
            }
            LanguageType.ENGLISH.code -> {
                message?.messageen
            }
            else -> message?.message
        } ?: ""
    }
}