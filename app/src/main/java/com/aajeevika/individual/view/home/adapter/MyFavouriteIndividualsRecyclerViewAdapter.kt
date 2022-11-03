package com.aajeevika.individual.view.home.adapter

import BaseUrls
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemMyFavouriteIndividualsBinding
import com.aajeevika.individual.model.data_model.FavSellerData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.view.profile.ActivityIndividualProfile

class MyFavouriteIndividualsRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<FavSellerData>()

    fun addData(data: ArrayList<FavSellerData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ListItemMyFavouriteIndividualsViewHolder(ListItemMyFavouriteIndividualsBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ListItemMyFavouriteIndividualsViewHolder(val dataBinding: ListItemMyFavouriteIndividualsBinding): BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                userName = data.name
                profileImage = data.profileImage?.run { BaseUrls.baseUrl + this }

                root.setOnClickListener {
                    val intent = Intent(context, ActivityIndividualProfile::class.java)
                    intent.putExtra(Constant.USER_ID, data.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}