package com.aajeevika.individual.view.sale

import android.content.Intent
import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySalesBinding
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.view.sale.adapter.SalesRecyclerViewAdapter
import com.aajeevika.individual.view.sale.viewmodel.SalesViewModel

class ActivitySales : BaseActivityVM<ActivitySalesBinding, SalesViewModel>(
    R.layout.activity_sales,
    SalesViewModel::class
) {
    private val salesRecyclerViewAdapter = SalesRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = salesRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSalesList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.salesLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            salesRecyclerViewAdapter.addData(it)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            addNewSaleBtn.setOnClickListener {
                val intent = Intent(this@ActivitySales, ActivityAddNewSale::class.java)
                startActivity(intent)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getSalesList()
            }
        }
    }
}