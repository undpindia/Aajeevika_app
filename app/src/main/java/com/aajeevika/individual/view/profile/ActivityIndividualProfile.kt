package com.aajeevika.individual.view.profile

import BaseUrls
import android.content.Intent
import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityIndividualProfileBinding
import com.aajeevika.individual.model.data_model.UserData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.view.chat.ActivityConversation
import com.aajeevika.individual.view.profile.adapter.UserInterestRecyclerViewAdapter
import com.aajeevika.individual.view.profile.viewmodel.IndividualProfileViewModel

class ActivityIndividualProfile :
    BaseActivityVM<ActivityIndividualProfileBinding, IndividualProfileViewModel>(
        R.layout.activity_individual_profile,
        IndividualProfileViewModel::class
    ) {
    private lateinit var userData: UserData
    private val userInterestRecyclerViewAdapter = UserInterestRecyclerViewAdapter(preferencesHelper)
    private val individualId: Int by lazy { intent.getIntExtra(Constant.USER_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = userInterestRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(16F, 16F, 16F, 16F))
        }

        viewModel.getIndividualProfile(individualId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.requestStatusLiveData.observe(this, {
            viewDataBinding.isFav = !viewDataBinding.isFav
            viewDataBinding.executePendingBindings()
        })

        viewModel.userLiveData.observe(this, {
            it.user?.run { userData = this }

            viewDataBinding.run {
                val addressStringBuilder = StringBuilder()

                it.address?.registered?.run {
                    address_line_one?.run { addressStringBuilder.append("$this, ") }
                    address_line_two?.run { addressStringBuilder.append("$this, ") }
                    district?.run { addressStringBuilder.append("$this, ") }
                    state?.run { addressStringBuilder.append("$this, ") }
                    country?.run { addressStringBuilder.append("$this, ") }
                    block?.run { addressStringBuilder.append("$this, ") }
                    pincode?.run { addressStringBuilder.append(this) }
                }

                mobileNumber = it.user?.mobile
                name = it.user?.name
                emailId = it.user?.email
                isFav = it.favStatus == 1
                address = addressStringBuilder.toString()
                profileImage = it.user?.profileImage?.run { BaseUrls.baseUrl + this }

                totalRating = it.rating?.reviewCount?.toInt() ?: 0
                rating = it.rating?.ratingAvgStar?.toFloat() ?: 0F
                executePendingBindings()
            }

            userInterestRecyclerViewAdapter.addData(it.individualInterest)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            toolbar.favBtn.setOnClickListener {
                if (::userData.isInitialized)
                    userData.id?.let { viewModel.addFavorite(it, if (isFav) 0 else 1) }
            }

            ratingBtn.setOnClickListener {
                val intent =
                    Intent(this@ActivityIndividualProfile, ActivityRatingAndReviews::class.java)
                intent.putExtra(Constant.USER_ID, userData.id)
                startActivity(intent)
            }
            chatBtn.setOnClickListener {
                val intent =
                    Intent(this@ActivityIndividualProfile, ActivityConversation::class.java)
                intent.putExtra(Constant.USER_ID, userData.id.toString())
                startActivity(intent)
            }
        }
    }
}