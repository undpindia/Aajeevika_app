package com.aajeevika.individual.view.profile

import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityRatingAndReviewsBinding
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.view.profile.adapter.RatingAndReviewsRecyclerViewAdapter
import com.aajeevika.individual.view.profile.viewmodel.RatingAndReviewsViewModel

class ActivityRatingAndReviews : BaseActivityVM<ActivityRatingAndReviewsBinding, RatingAndReviewsViewModel>(
    R.layout.activity_rating_and_reviews,
    RatingAndReviewsViewModel::class
) {
    private val userId: Int by lazy { intent.getIntExtra(Constant.USER_ID, -1) }

    private val ratingAndReviewsRecyclerViewAdapter = RatingAndReviewsRecyclerViewAdapter {
        viewModel.getReviews(userId, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.recyclerView.adapter = ratingAndReviewsRecyclerViewAdapter

        viewModel.getReviews(userId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.reviewLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            ratingAndReviewsRecyclerViewAdapter.addData(it.ratings, it.pagination.current_page, it.pagination.last_page)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getReviews(userId)
            }
        }
    }
}