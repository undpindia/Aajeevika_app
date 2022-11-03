package com.aajeevika.individual.view.sale

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityAddProductBinding
import com.aajeevika.individual.model.data_model.IndProductData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.utility.UtilityActions.showMessage
import com.aajeevika.individual.view.sale.adapter.CategoryWiseProductRecyclerViewAdapter
import com.aajeevika.individual.view.sale.viewmodel.AddProductViewModel
import java.util.*

class ActivityAddProduct : BaseActivityVM<ActivityAddProductBinding, AddProductViewModel>(
    R.layout.activity_add_product,
    AddProductViewModel::class
) {
    private lateinit var timer: Timer
    private var oldSearch = ""

    private val categoryWiseProductRecyclerViewAdapter = CategoryWiseProductRecyclerViewAdapter {
        viewModel.addNewProductList(it, oldSearch)
    }

    private val productData: ArrayList<IndProductData>? by lazy {
        intent.getSerializableExtra(Constant.PRODUCT_DATA) as? ArrayList<IndProductData>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = categoryWiseProductRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(16F, 6F, 16F, 6F))
        }

        viewModel.addNewProductList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.productLiveData.observe(this, { data ->
            data.catProduct.forEach { catList ->
                catList.ind_products.forEach { product ->
                    productData?.firstOrNull { it.id == product.id }?.quantity?.let { product.quantity = it }
                }
            }

            stopSwipeToRefreshRefresh()
            categoryWiseProductRecyclerViewAdapter.addData(data.catProduct, data.pagination.current_page, data.pagination.last_page)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            inputSearch.doOnTextChanged { text, _, _, _ ->
                if (::timer.isInitialized) timer.cancel()
                categoryWiseProductRecyclerViewAdapter.resetData()

                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            oldSearch = text.toString().trim()
                            viewModel.addNewProductList(search = oldSearch)
                        }
                    }
                }, 200L)
            }

            doneBtn.setOnClickListener {
                if(categoryWiseProductRecyclerViewAdapter.getDataList().isEmpty()) {
                    root.showMessage(getString(R.string.please_enter_at_least_one_product))
                } else {
                    val intent = Intent()
                    intent.putExtra(Constant.PRODUCT_DATA, categoryWiseProductRecyclerViewAdapter.getDataList())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.addNewProductList(search = oldSearch)
            }
        }
    }

    override fun onDestroy() {
        if (::timer.isInitialized) timer.cancel()
        super.onDestroy()
    }
}