package com.aajeevika.individual.view.survey

import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySurveyBinding
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.view.survey.adapter.SurveyRecyclerViewAdapter
import com.aajeevika.individual.view.survey.viewmodel.SurveyViewModel

class ActivitySurvey: BaseActivityVM<ActivitySurveyBinding, SurveyViewModel>(
    R.layout.activity_survey,
    SurveyViewModel::class
) {
    private val surveyRecyclerViewAdapter by lazy { SurveyRecyclerViewAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = surveyRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F,8F,8F,8F))
        }

        viewModel.getSurveyList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.surveyListLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            surveyRecyclerViewAdapter.addData(it)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getSurveyList()
            }
        }
    }
}