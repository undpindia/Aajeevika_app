package com.aajeevika.individual.view.suvidha

import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySadhanBinding
import com.aajeevika.individual.databinding.ActivitySurveyBinding
import com.aajeevika.individual.databinding.ActivitySuvidhaBinding
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.view.suvidha.adapter.SadhanRecyclerViewAdapter
import com.aajeevika.individual.view.suvidha.adapter.SuvidhaRecyclerViewAdapter
import com.aajeevika.individual.view.suvidha.viewmodel.SadhanViewModel
import com.aajeevika.individual.view.suvidha.viewmodel.SuvidhaViewModel

class ActivitySadhan : BaseActivityVM<ActivitySadhanBinding, SadhanViewModel>(R.layout.activity_sadhan, SadhanViewModel::class) {
    private val sadhanRecyclerViewAdapter = SadhanRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = sadhanRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(0F,10F,0F,10F))
        }

        viewModel.getSadhanList()
    }

    override fun observeData() {
        super.observeData()
        viewModel.sadhanListResponse.observe(this, {
            stopSwipeToRefreshRefresh()
            sadhanRecyclerViewAdapter.addData(it)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getSadhanList()
            }
        }
    }
}