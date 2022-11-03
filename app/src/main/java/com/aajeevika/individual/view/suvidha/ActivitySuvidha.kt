package com.aajeevika.individual.view.suvidha

import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySurveyBinding
import com.aajeevika.individual.databinding.ActivitySuvidhaBinding
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.view.suvidha.adapter.SuvidhaRecyclerViewAdapter
import com.aajeevika.individual.view.suvidha.viewmodel.SuvidhaViewModel

class ActivitySuvidha : BaseActivityVM<ActivitySuvidhaBinding, SuvidhaViewModel>(R.layout.activity_suvidha, SuvidhaViewModel::class) {
    private val suvidhaRecyclerViewAdapter = SuvidhaRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = suvidhaRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.getSuvidhaList()
    }

    override fun observeData() {
        super.observeData()
        viewModel.suvidhaListResponse.observe(this, {
            stopSwipeToRefreshRefresh()
            suvidhaRecyclerViewAdapter.addData(it)
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