package com.aajeevika.individual.view.other

import android.os.Bundle
import androidx.lifecycle.Observer
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityFaqQuestionBinding
import com.aajeevika.individual.databinding.ActivityFaqsBinding
import com.aajeevika.individual.model.data_model.FaqQuestions
import com.aajeevika.individual.view.other.adapter.FaqsQuestionAdapter
import com.aajeevika.individual.view.other.adapter.FaqsRecyclerViewAdapter
import com.aajeevika.individual.view.other.viewmodel.FaqViewModel

class ActivityFaqQuestion : BaseActivityVM<ActivityFaqQuestionBinding, FaqViewModel>(
    R.layout.activity_faq_question,
    FaqViewModel::class
) {
    private val faqsAdapter: FaqsQuestionAdapter by lazy {
        FaqsQuestionAdapter(mList)
    }

    private val mList: ArrayList<FaqQuestions> by lazy {
        intent.getSerializableExtra("ques") as? ArrayList<FaqQuestions> ?: arrayListOf()
    }

    private val mTitle:String by lazy { intent.getStringExtra("title")?:"" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.title = mTitle
        viewDataBinding.recyclerView.adapter = faqsAdapter
    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        viewDataBinding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}