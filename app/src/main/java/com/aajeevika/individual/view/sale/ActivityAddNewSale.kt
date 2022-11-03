package com.aajeevika.individual.view.sale

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityAddNewSaleBinding
import com.aajeevika.individual.model.data_model.ClfData
import com.aajeevika.individual.model.data_model.IndProductData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.utility.UtilityActions.showMessage
import com.aajeevika.individual.view.dialog.AlertDialog
import com.aajeevika.individual.view.sale.adapter.SaleProductRecyclerViewAdapter
import com.aajeevika.individual.view.sale.viewmodel.AddNewSaleViewModel
import java.util.*
import kotlin.collections.ArrayList

class ActivityAddNewSale : BaseActivityVM<ActivityAddNewSaleBinding, AddNewSaleViewModel>(
    R.layout.activity_add_new_sale,
    AddNewSaleViewModel::class
) {
    private val saleProductRecyclerViewAdapter = SaleProductRecyclerViewAdapter()
    private val productsList = ArrayList<IndProductData>()
    private lateinit var clfData: ClfData

    private val addProductResultCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            (it.data?.getSerializableExtra(Constant.PRODUCT_DATA) as? ArrayList<IndProductData>)?.let { list ->
                productsList.clear()
                productsList.addAll(list)
                saleProductRecyclerViewAdapter.addData(productsList)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.dateOfSaleValue.text = UtilityActions.formatDate(Calendar.getInstance().time)

        viewModel.getClfList()

        viewDataBinding.recyclerView.run {
            adapter = saleProductRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.clfListLiveData.observe(this, { list ->
            viewDataBinding.clfNameList = list.map { it.name }.toCollection(ArrayList<String>())
        })

        viewModel.saleStatusLiveData.observe(this, {
            AlertDialog(
                context = this@ActivityAddNewSale,
                cancelOnOutsideClick = false,
                message = it ?: "",
                positive = getString(R.string.ok),
                positiveClick = { onBackPressed() }
            ).show()
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            inputName.doOnTextChanged { text, _, _, _ ->
                viewModel.clfListLiveData.value?.firstOrNull { it.name == text?.toString()?.trim() }?.let { data ->
                    clfData = data

                    clfPhoneNumber = data.mobile
                    clfEmailAddress = data.email
                    executePendingBindings()
                }
            }

            addProductsBtn.setOnClickListener {
                if (::clfData.isInitialized) {
                    val intent = Intent(this@ActivityAddNewSale, ActivityAddProduct::class.java)
                    intent.putExtra(Constant.PRODUCT_DATA, productsList)
                    addProductResultCallback.launch(intent)
                }
            }

            submitBtn.setOnClickListener {
                val review = inputReview.text.toString().trim()
                val rating = ratingBar.rating

                validateFormData(rating, review)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    val saleDate = dateOfSaleValue.text.toString()
                    viewModel.addNewSale(clfData.id, rating, review, saleDate, productsList)
                }
            }

            dateOfSaleContainer.setOnClickListener {
                UtilityActions.parseDate(dateOfSaleValue.text.toString())?.let { date ->
                    val calendar = Calendar.getInstance()
                    calendar.time = date

                    val datePickerDialog = DatePickerDialog(
                        this@ActivityAddNewSale,
                        { _, year, month, dayOfMonth ->
                            calendar.set(Calendar.YEAR, year)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            viewDataBinding.dateOfSaleValue.text = UtilityActions.formatDate(calendar.time)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.maxDate = Calendar.getInstance().timeInMillis
                    }

                    datePickerDialog.show()
                }
            }
        }
    }

    private fun validateFormData(rating: Float, review: String): String? {
        return when {
            !::clfData.isInitialized -> getString(R.string.select_a_clf)
            productsList.isEmpty() -> getString(R.string.please_enter_at_least_one_product)
            review.isEmpty() -> getString(R.string.please_add_a_review_for_this_sale)
            rating == 0F -> getString(R.string.please_rate_this_sale)
            else -> null
        }
    }
}