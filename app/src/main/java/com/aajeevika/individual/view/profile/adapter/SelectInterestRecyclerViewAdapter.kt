package com.aajeevika.individual.view.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemInterestBinding
import com.aajeevika.individual.databinding.ListItemInterestVertBinding
import com.aajeevika.individual.model.data_model.InterestData
import java.lang.StringBuilder

class SelectInterestRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<InterestData>()
    private val selectedInterests = ArrayList<Int>()

    fun addData(data: ArrayList<InterestData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun getDataSize() = dataList.size

    fun getSelectedInterests(): String {
        val ids = StringBuilder()

        for (index in selectedInterests.indices) {
            ids.append(selectedInterests[index])
            if(index < (selectedInterests.size - 1)) ids.append(",")
        }

        return ids.toString()
    }

    fun selectedInterestSize() = selectedInterests.size

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ListItemInterestViewHolder(ListItemInterestVertBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class ListItemInterestViewHolder(val dataBinding: ListItemInterestVertBinding) : BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                interestName = data.name
                interestImage = BaseUrls.baseUrl + data.image
                isSelected = selectedInterests.contains(data.id)

                root.setOnClickListener {
                    if(selectedInterests.contains(data.id)) selectedInterests.remove(data.id)
                    else selectedInterests.add(data.id)
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }
}