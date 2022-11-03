package com.aajeevika.individual.view.sale

import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySaleDetailBinding
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.view.sale.adapter.SaleDetailProductsRecyclerViewAdapter
import com.aajeevika.individual.view.sale.viewmodel.SaleDetailViewModel

class ActivitySaleDetail : BaseActivityVM<ActivitySaleDetailBinding, SaleDetailViewModel>(
    R.layout.activity_sale_detail,
    SaleDetailViewModel::class
) {
    private val saleDetailProductsRecyclerViewAdapter = SaleDetailProductsRecyclerViewAdapter()
    private val orderId by lazy { intent.getIntExtra(Constant.ORDER_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = saleDetailProductsRecyclerViewAdapter
        }

        viewModel.getIndividualSaleDetails(orderId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.saleDetailLiveData.observe(this, {
            saleDetailProductsRecyclerViewAdapter.addData(it.order_details.ind_items)

            viewDataBinding.run {
                buyerName = it.order_details.get_clf.name
                buyerId = it.order_details.order_id_d

                if(it.order_details.created_at.isNotEmpty()) {
                    UtilityActions.parseInterestDate(it.order_details.created_at)?.let { updateDate ->
                        date = UtilityActions.formatInterestDate(updateDate)
                    }
                }

                //ind_rating indicates that the clf rated individual. So, don't replace "ind_rating" with "clf_rating"
                clfProfileImage = it.order_details.get_clf.profileImage?.run { BaseUrls.baseUrl + this }
                clfName = it.order_details.get_clf.name
                clfRating = it.order_details.ind_rating?.rating ?: 0F
                clfMessage = it.order_details.ind_rating?.review_msg

                //clf_rating indicates that the individual rated clf. So, don't replace "clf_rating" with "ind_rating"
                individualProfileImage = it.order_details.get_individual.profileImage?.run { BaseUrls.baseUrl + this }
                individualName = it.order_details.get_individual.name
                individualRating = it.order_details.clf_rating?.rating ?: 0F
                individualMessage = it.order_details.clf_rating?.review_msg

                executePendingBindings()
            }
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }
}