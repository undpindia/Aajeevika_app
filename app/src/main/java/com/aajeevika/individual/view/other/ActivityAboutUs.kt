package com.aajeevika.individual.view.other

import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivity
import com.aajeevika.individual.databinding.ActivityAboutUsBinding

class ActivityAboutUs : BaseActivity<ActivityAboutUsBinding>(R.layout.activity_about_us) {
    override fun initListener() {
        viewDataBinding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}