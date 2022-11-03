package com.aajeevika.individual.view.sale.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemSaleAddProductBinding
import com.aajeevika.individual.model.data_model.IndProductData

class SaleAddProductRecyclerViewAdapter(private val dataList: ArrayList<IndProductData>) : BaseRecyclerViewAdapter() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        ProductViewHolder(ListItemSaleAddProductBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    inner class ProductViewHolder(val dataBinding: ListItemSaleAddProductBinding): BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                productImage = BaseUrls.baseUrl + data.image
                quantity = data.quantity.toString()
                productName = data.name
                priceUnit = data.price_unit

                removeBtn.setOnClickListener {
                    if(data.quantity > 0) {
                        data.quantity--
                        quantity = data.quantity.toString()
                        executePendingBindings()
                    }
                }

                addBtn.setOnClickListener {
                    data.quantity++
                    quantity = data.quantity.toString()
                    executePendingBindings()
                }
            }
        }
    }
}