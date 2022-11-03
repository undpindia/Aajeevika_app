package com.aajeevika.individual.view.sale.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemCategoryWiseProductsBinding
import com.aajeevika.individual.databinding.ListItemLoadMoreBinding
import com.aajeevika.individual.model.data_model.CatProductData
import com.aajeevika.individual.model.data_model.IndProductData
import com.aajeevika.individual.utility.RecyclerViewDecoration

class CategoryWiseProductRecyclerViewAdapter(private val requestData: (Int) -> Unit) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<CatProductData>()
    private var isNextPageRequested = false
    private var currentPage: Int = -1
    private var lastPage: Int = -1

    fun addData(data: ArrayList<CatProductData>, currentPage: Int, lastPage: Int) {
        isNextPageRequested = false

        this.currentPage = currentPage
        this.lastPage = lastPage

        if(currentPage == 1) dataList.clear()
        val currentDataLisSize = dataList.size
        dataList.addAll(data)

        if(currentPage == 1) notifyDataSetChanged()
        else notifyItemRangeChanged(currentDataLisSize, dataList.size - currentDataLisSize)
    }

    fun resetData() {
        dataList.clear()
        isNextPageRequested = false
        currentPage = -1
        lastPage = -1

        notifyDataSetChanged()
    }

    fun getDataList(): ArrayList<IndProductData> {
        val productList = ArrayList<IndProductData>()

        dataList.forEach {
            it.ind_products.forEach { product ->
                if(product.quantity > 0) productList.add(product)
            }
        }

        return productList
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastVisibleItemPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()

                if(lastVisibleItemPosition == dataList.size && !isNextPageRequested) {
                    isNextPageRequested = true
                    requestData(currentPage + 1)
                }
            }
        })
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder? = run {
        when(viewType) {
            1 -> CategoryWiseProductViewHolder(ListItemCategoryWiseProductsBinding.inflate(inflater, parent, false))
            0 -> LoadMoreViewHolder(ListItemLoadMoreBinding.inflate(inflater, parent, false))
            else -> null
        }
    }

    override fun getItemCount() = dataList.size + if(currentPage < lastPage) 1 else 0

    override fun getItemViewType(position: Int) = if(position < dataList.size) 1 else 0

    inner class CategoryWiseProductViewHolder(val dataBinding: ListItemCategoryWiseProductsBinding): BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            dataBinding.run {
                title = data.name

                recyclerView.run {
                    adapter = SaleAddProductRecyclerViewAdapter(data.ind_products)

                    if(itemDecorationCount == 0)
                        addItemDecoration(RecyclerViewDecoration(0F, 8F, 0F, 8F))
                }
            }
        }
    }

    private inner class LoadMoreViewHolder(private val viewDataBinding: ListItemLoadMoreBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {}
    }
}