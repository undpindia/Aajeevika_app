package com.aajeevika.individual.view.suvidha

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySarvottamPrathayeBinding
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.view.suvidha.adapter.SarcottamPrathanyeRecyclerViewAdapter
import com.aajeevika.individual.view.suvidha.viewmodel.SarvottamPrathayeViewModel

class ActivitySarvottamPrathaye : BaseActivityVM<ActivitySarvottamPrathayeBinding, SarvottamPrathayeViewModel>(R.layout.activity_sarvottam_prathaye, SarvottamPrathayeViewModel::class) {
    private val permissionListener = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
    private val sarcottamPrathanyeRecyclerViewAdapter = SarcottamPrathanyeRecyclerViewAdapter(permissionListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.run {
            adapter = sarcottamPrathanyeRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.getSuvidhaList()
    }

    override fun observeData() {
        super.observeData()
        viewModel.sarvottamPrathayeListResponse.observe(this, {
            stopSwipeToRefreshRefresh()
            sarcottamPrathanyeRecyclerViewAdapter.addData(it)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getSuvidhaList()
            }
        }
    }
}