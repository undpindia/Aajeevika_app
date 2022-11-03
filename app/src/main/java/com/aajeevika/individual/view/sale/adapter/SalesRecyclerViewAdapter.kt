package com.aajeevika.individual.view.sale.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemSalesBinding
import com.aajeevika.individual.model.data_model.SaleData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.view.sale.ActivitySaleDetail

class SalesRecyclerViewAdapter : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SaleData>()

    fun addData(data: ArrayList<SaleData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        SalesViewHolder(ListItemSalesBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class SalesViewHolder(private val viewDataBinding: ListItemSalesBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            viewDataBinding.run {
                name = data.get_clf.organization_name
                orderId = data.order_id_d

                if(data.created_at.isNotEmpty()) {
                    UtilityActions.parseInterestDate(data.created_at)?.let { updateDate ->
                        date = UtilityActions.formatInterestDate(updateDate)
                    }
                }

                root.setOnClickListener {
                    val intent = Intent(context, ActivitySaleDetail::class.java)
                    intent.putExtra(Constant.ORDER_ID, data.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}