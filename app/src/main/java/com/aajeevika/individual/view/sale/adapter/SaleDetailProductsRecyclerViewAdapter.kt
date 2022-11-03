package com.aajeevika.individual.view.sale.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemSaleDetailsProductBinding
import com.aajeevika.individual.model.data_model.BuyDetailItemData

class SaleDetailProductsRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<BuyDetailItemData>()

    fun addData(data: ArrayList<BuyDetailItemData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        BuyDetailProductViewHolder(ListItemSaleDetailsProductBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class BuyDetailProductViewHolder(val dataBinding: ListItemSaleDetailsProductBinding): BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                name = data.indproduct.name_en
                quantity = data.quantity.toString()
                priceUnit = data.indproduct.price_unit
            }
        }
    }
}