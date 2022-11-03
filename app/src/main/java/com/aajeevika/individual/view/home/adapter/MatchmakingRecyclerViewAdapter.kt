package com.aajeevika.individual.view.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemMatchingSellerBinding
import com.aajeevika.individual.model.data_model.MatchingSellerData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.view.profile.ActivityIndividualProfile

class MatchmakingRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<MatchingSellerData>()

    fun addData(data: ArrayList<MatchingSellerData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ListItemMatchingSellerViewHolder(ListItemMatchingSellerBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ListItemMatchingSellerViewHolder(val dataBinding: ListItemMatchingSellerBinding): BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                userName = data.name
                profileImage = data.profileImage?.run { BaseUrls.baseUrl + this }
                (root.layoutParams as RecyclerView.LayoutParams).width = (UtilityActions.windowWidth() * 0.4).toInt()

                root.setOnClickListener {
                    val intent = Intent(context, ActivityIndividualProfile::class.java)
                    intent.putExtra(Constant.USER_ID, data.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}