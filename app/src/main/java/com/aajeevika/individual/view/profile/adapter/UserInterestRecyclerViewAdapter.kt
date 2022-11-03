package com.aajeevika.individual.view.profile.adapter

import BaseUrls
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemInterestBinding
import com.aajeevika.individual.model.data_model.UserInterests
import com.aajeevika.individual.utility.AppPreferencesHelper
import com.aajeevika.individual.utility.app_enum.LanguageType

class UserInterestRecyclerViewAdapter(private val preferencesHelper: AppPreferencesHelper) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<UserInterests>()

    fun addData(data: ArrayList<UserInterests>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<UserInterests> = dataList

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ListItemInterestViewHolder(ListItemInterestBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class ListItemInterestViewHolder(val dataBinding: ListItemInterestBinding) : BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                interestName = if(preferencesHelper.appLanguage == LanguageType.ENGLISH.code) data.name_en else data.name_hi
                interestImage = data.image?.run { BaseUrls.baseUrl + this }
            }
        }
    }
}