package com.aajeevika.individual.view.other

import android.os.Bundle
import androidx.lifecycle.Observer
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityFaqsBinding
import com.aajeevika.individual.view.other.adapter.FaqsRecyclerViewAdapter
import com.aajeevika.individual.view.other.viewmodel.FaqViewModel

class ActivityFaqs : BaseActivityVM<ActivityFaqsBinding, FaqViewModel>(
    R.layout.activity_faqs,
    FaqViewModel::class
) {
    private val faqsAdapter = FaqsRecyclerViewAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = faqsAdapter

        viewModel.getAllFaq()
    }

    override fun observeData() {
        super.observeData()
        viewModel.faq.observe(this, Observer {
            faqsAdapter.mList.clear()
            faqsAdapter.mList.addAll(it)
            faqsAdapter.notifyDataSetChanged()
        })
    }

    override fun initListener() {
        viewDataBinding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}