package com.aajeevika.individual.view.suvidha.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemSuvidhaBinding
import com.aajeevika.individual.model.data_model.SuvidhaData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.view.suvidha.ActivitySuvidhaDetails

class SuvidhaRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SuvidhaData>()

    fun addData(data: ArrayList<SuvidhaData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        SuvidhaListViewHolder(ListItemSuvidhaBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class SuvidhaListViewHolder(val dataBinding: ListItemSuvidhaBinding) : BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            dataBinding.run {
                dataList[adapterPosition].let { data ->
                    data.image1?.let { image = BaseUrls.baseUrl + it }
                    title = data.title
                    subTitle = data.desc
                }

                root.setOnClickListener {
                    val intent = Intent(context, ActivitySuvidhaDetails::class.java)
                    intent.putExtra(Constant.SUVIDHADATA, dataList[adapterPosition])
                    context.startActivity(intent)
                }
            }
        }
    }
}