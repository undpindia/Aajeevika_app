package com.aajeevika.individual.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySelectInterestBinding
import com.aajeevika.individual.model.data_model.InterestData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.utility.UtilityActions.showMessage
import com.aajeevika.individual.view.home.ActivityDashboard
import com.aajeevika.individual.view.profile.adapter.SelectInterestRecyclerViewAdapter
import com.aajeevika.individual.view.profile.viewmodel.SelectInterestViewModel

class ActivitySelectInterest: BaseActivityVM<ActivitySelectInterestBinding, SelectInterestViewModel>(
    R.layout.activity_select_interest,
    SelectInterestViewModel::class
) {
    private val selectInterestRecyclerViewAdapter by lazy { SelectInterestRecyclerViewAdapter() }
    private val isEditing by lazy { intent.getBooleanExtra(Constant.EDIT, false) }
    private lateinit var interestsList: ArrayList<InterestData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = selectInterestRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(16F, 8F, 16F, 8F))
        }

        viewModel.getInterestList()
    }

    override fun observeData() {
        super.observeData()

        viewModel.interestList.observe(this, {
            interestsList = it
            stopSwipeToRefreshRefresh()
            viewDataBinding.inputSearch.isEnabled = true

            if((viewDataBinding.inputSearch.text?.length ?: 0) > 0)
                selectInterestRecyclerViewAdapter.addData(
                    interestsList.filter { it.name.contains(viewDataBinding.inputSearch.text.toString(), true) }.toCollection(ArrayList())
                )
            else
                selectInterestRecyclerViewAdapter.addData(interestsList)

            if(selectInterestRecyclerViewAdapter.getDataSize() == 0) {
                onErrorReturn(getString(R.string.no_data_found))
            } else {
                onErrorReturn(null)
            }
        })

        viewModel.requestStatusLiveData.observe(this, {
            if(isEditing) {
                finish()
            } else {
                val intent = Intent(this@ActivitySelectInterest, ActivityDashboard::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

    override fun onBackPressed() {
        if((viewDataBinding.inputSearch.text?.length ?: 0) > 0) {
            viewDataBinding.inputSearch.text = null
        } else {
            super.onBackPressed()
        }
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                finish()
            }

            proceedBtn.setOnClickListener {
                if(selectInterestRecyclerViewAdapter.selectedInterestSize() != 5) {
                    viewDataBinding.root.showMessage(getString(R.string.select_five_interests))
                } else {
                    viewModel.addInterestList(preferencesHelper.uid, selectInterestRecyclerViewAdapter.getSelectedInterests())
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getInterestList()
            }

            viewDataBinding.inputSearch.doOnTextChanged { text, _, _, _ ->
                if((text?.length ?: 0) > 0)
                    selectInterestRecyclerViewAdapter.addData(
                        interestsList.filter { it.name.contains(text.toString(), true) }.toCollection(ArrayList())
                    )
                else
                    selectInterestRecyclerViewAdapter.addData(interestsList)

                if(selectInterestRecyclerViewAdapter.getDataSize() == 0) {
                    onErrorReturn(getString(R.string.no_data_found))
                } else {
                    onErrorReturn(null)
                }
            }
        }
    }
}