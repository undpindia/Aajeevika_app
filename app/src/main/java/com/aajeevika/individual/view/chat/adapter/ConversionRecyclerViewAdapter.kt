package com.aajeevika.individual.view.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemMyChatOtherBinding
import com.aajeevika.individual.databinding.ListItemMyChatSelfBinding
import com.aajeevika.individual.model.data_model.MessageModel
import com.aajeevika.individual.utility.app_enum.LanguageType
import com.aajeevika.individual.utility.extension.setFormatTime

class ConversionRecyclerViewAdapter(
    private val mUserId: String,
    private val mUserLanguage: String
) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<MessageModel>()

    fun addData(data: ArrayList<MessageModel>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun add(data: MessageModel) {
        dataList.add(data)
        notifyItemChanged(itemCount)
    }

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return when (viewType) {
            0 -> MyChatSelfViewHolder(ListItemMyChatSelfBinding.inflate(inflater, parent, false))
            else -> MyChatOtherViewHolder(
                ListItemMyChatOtherBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = dataList.size
    override fun getItemViewType(position: Int): Int {
        return when (dataList[position].fromUserId == mUserId) {
            true -> 0
            false -> 1
        }
    }

    inner class MyChatSelfViewHolder(val dataBinding: ListItemMyChatSelfBinding) :
        BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            dataBinding.run {
                val data = dataList[adapterPosition]
                message = when (mUserLanguage) {
                    LanguageType.HINDI.code -> data.messagehi
                    else -> data.messageen
                }
                time = data.timestamp.setFormatTime()
            }
        }
    }

    inner class MyChatOtherViewHolder(val dataBinding: ListItemMyChatOtherBinding) :
        BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            dataBinding.run {
                val data = dataList[adapterPosition]
                message = when (mUserLanguage) {
                    LanguageType.HINDI.code -> data.messagehi
                    else -> data.messageen
                }
                time = data.timestamp.setFormatTime()
            }
        }
    }
}