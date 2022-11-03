package com.aajeevika.individual.view.other

import android.os.Bundle
import androidx.lifecycle.Observer
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityFaqDescriptionBinding
import com.aajeevika.individual.databinding.ActivityFaqsBinding
import com.aajeevika.individual.model.data_model.FaqQuestions
import com.aajeevika.individual.view.other.adapter.FaqsQuestionAdapter
import com.aajeevika.individual.view.other.adapter.FaqsRecyclerViewAdapter
import com.aajeevika.individual.view.other.viewmodel.FaqViewModel

class ActivityFaqDescription : BaseActivityVM<ActivityFaqDescriptionBinding, FaqViewModel>(
    R.layout.activity_faq_description,
    FaqViewModel::class
) {
    private val mTitle:String by lazy { intent.getStringExtra("title")?:"" }
    private val desc:String by lazy { intent.getStringExtra("desc")?:"" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.title = mTitle
        viewDataBinding.desc = desc
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