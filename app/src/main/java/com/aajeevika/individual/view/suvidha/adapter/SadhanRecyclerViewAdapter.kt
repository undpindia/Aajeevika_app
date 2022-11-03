package com.aajeevika.individual.view.suvidha.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemSadhanBinding
import com.aajeevika.individual.model.data_model.SadhanData
import com.aajeevika.individual.utility.RecyclerViewDecoration

class SadhanRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SadhanData>()

    fun addData(data: ArrayList<SadhanData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        SadhanListViewHolder(ListItemSadhanBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class SadhanListViewHolder(val dataBinding: ListItemSadhanBinding) : BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                title = data.catName
                recyclerView.adapter = SadhanVideoRecyclerViewAdapter(data.get_sadhan)
                if(recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(RecyclerViewDecoration(6F, 6F, 6F, 6F))
            }
        }
    }
}