package com.aajeevika.individual.view.suvidha

import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySuvidhaDetailsBinding
import com.aajeevika.individual.model.data_model.SuvidhaData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.view.suvidha.adapter.SuvidhaViewPager
import com.aajeevika.individual.view.suvidha.viewmodel.SuvidhaViewModel
import kotlin.math.abs

class ActivitySuvidhaDetails : BaseActivityVM<ActivitySuvidhaDetailsBinding, SuvidhaViewModel>(R.layout.activity_suvidha_details, SuvidhaViewModel::class) {
    private val suvidhaData by lazy { intent.getSerializableExtra(Constant.SUVIDHADATA) as? SuvidhaData }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        suvidhaData?.run {
            val imageArray = ArrayList<String>()
            image1?.let { imageArray.add(it) }
            image2?.let { imageArray.add(it) }
            image3?.let { imageArray.add(it) }
            image4?.let { imageArray.add(it) }

            viewDataBinding.title = title
            viewDataBinding.description = desc

            if(imageArray.isNotEmpty()) {
                viewDataBinding.showPager = true

                viewDataBinding.viewPager.adapter = SuvidhaViewPager(layoutInflater, imageArray)

                viewDataBinding.viewPager.pageMargin = 16
                viewDataBinding.viewPager.setPageTransformer(false) { page, position ->
                    page.scaleX = 1F - 0.07F * abs(position)
                    page.scaleY = 1F - 0.25F * abs(position)
                }

                viewDataBinding.tabLayout.setupWithViewPager(viewDataBinding.viewPager)
            }
        }
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }
}