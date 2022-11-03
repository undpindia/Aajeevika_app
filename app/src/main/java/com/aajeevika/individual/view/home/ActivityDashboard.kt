package com.aajeevika.individual.view.home

import BaseUrls
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.aajeevika.individual.BuildConfig
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivityDashboardBinding
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.RecyclerViewDecoration
import com.aajeevika.individual.utility.UtilityActions
import com.aajeevika.individual.utility.app_enum.LanguageType
import com.aajeevika.individual.view.auth.ActivityLogin
import com.aajeevika.individual.view.chat.ActivityChat
import com.aajeevika.individual.view.dialog.AlertDialog
import com.aajeevika.individual.view.grievance.ActivityGrievance
import com.aajeevika.individual.view.home.adapter.MatchmakingRecyclerViewAdapter
import com.aajeevika.individual.view.home.adapter.MyFavouriteIndividualsRecyclerViewAdapter
import com.aajeevika.individual.view.home.viewmodel.DashboardViewModel
import com.aajeevika.individual.view.notification.ActivityNotifications
import com.aajeevika.individual.view.other.ActivityAboutUs
import com.aajeevika.individual.view.other.ActivityFaqs
import com.aajeevika.individual.view.other.ActivityWebView
import com.aajeevika.individual.view.profile.ActivityProfile
import com.aajeevika.individual.view.sale.ActivitySales
import com.aajeevika.individual.view.survey.ActivitySurvey
import com.aajeevika.individual.view.suvidha.ActivitySadhan
import com.aajeevika.individual.view.suvidha.ActivitySarvottamPrathaye
import com.aajeevika.individual.view.suvidha.ActivitySuvidha

class ActivityDashboard : BaseActivityVM<ActivityDashboardBinding, DashboardViewModel>(
    R.layout.activity_dashboard,
    DashboardViewModel::class
) {
    private val matchmakingRecyclerViewAdapter by lazy { MatchmakingRecyclerViewAdapter() }
    private val myFavouriteIndividualsRecyclerViewAdapter by lazy { MyFavouriteIndividualsRecyclerViewAdapter() }

    private val callRequestCallback = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it == true) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${BaseUrls.contactUsNumber}"))
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.run {
            userEmail = preferencesHelper.email
            userName = preferencesHelper.name
            version = BuildConfig.VERSION_NAME

            if(preferencesHelper.profileImage.isNotEmpty())
                userProfileImage = BaseUrls.baseUrl + preferencesHelper.profileImage

            if(preferencesHelper.appLanguage == LanguageType.ENGLISH.code) languageSwitch.isChecked = false
            else if(preferencesHelper.appLanguage == LanguageType.HINDI.code) languageSwitch.isChecked = true
        }

        viewDataBinding.matchingSellerRecyclerView.run {
            adapter = matchmakingRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 2F, 8F, 2F))
        }

        viewDataBinding.myFavIndividualRecyclerView.run {
            adapter = myFavouriteIndividualsRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getHomePage(preferencesHelper.uid)
        viewDataBinding.run {
            userEmail = preferencesHelper.email
            userName = preferencesHelper.name

            if(preferencesHelper.profileImage.isNotEmpty())
                userProfileImage = BaseUrls.baseUrl + preferencesHelper.profileImage
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.homeDataLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            viewDataBinding.myFavIndividualTxt.visibility = if(it.favUser.isEmpty()) View.GONE else View.VISIBLE

            matchmakingRecyclerViewAdapter.addData(it.matchingUser)
            myFavouriteIndividualsRecyclerViewAdapter.addData(it.favUser)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            menuBtn.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            notificationBtn.setOnClickListener {
                val intent = Intent(this@ActivityDashboard, ActivityNotifications::class.java)
                startActivity(intent)
            }

            profileContainer.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityProfile::class.java)
                startActivity(intent)
            }

            languageSwitch.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked && preferencesHelper.appLanguage != LanguageType.HINDI.code) {
                    preferencesHelper.appLanguage = LanguageType.HINDI.code

                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                } else if(!isChecked && preferencesHelper.appLanguage != LanguageType.ENGLISH.code) {
                    preferencesHelper.appLanguage = LanguageType.ENGLISH.code

                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                }
            }

            mySalesBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivitySales::class.java)
                startActivity(intent)
            }

            takeSurveyBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivitySurvey::class.java)
                startActivity(intent)
            }

            faqBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityFaqs::class.java)
                startActivity(intent)
            }

            grievanveBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityGrievance::class.java)
                startActivity(intent)
            }

            suvidhaBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivitySuvidha::class.java)
                startActivity(intent)
            }

            sadhanBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivitySadhan::class.java)
                startActivity(intent)
            }

            sarvottamPrathayeBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivitySarvottamPrathaye::class.java)
                startActivity(intent)
            }

            termsAndConditionsBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityWebView::class.java)
                intent.putExtra(Constant.TITLE, getString(R.string.terms_and_conditions))
                intent.putExtra(Constant.WEB_URL, BaseUrls.termsAndConditions)
                startActivity(intent)
            }

            privacyPolicyBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityWebView::class.java)
                intent.putExtra(Constant.TITLE, getString(R.string.privacy_policy))
                intent.putExtra(Constant.WEB_URL, BaseUrls.privacyPolicy)
                startActivity(intent)
            }

            rateUsBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                startActivity(intent)
            }

            supportBtn.setOnClickListener {
                callRequestCallback.launch(Manifest.permission.CALL_PHONE)
            }

            aboutUsBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityAboutUs::class.java)
                startActivity(intent)
            }

            myChatBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityChat::class.java)
                startActivity(intent)
            }

            logoutBtn.setOnClickListener {
                AlertDialog(
                    context = this@ActivityDashboard,
                    message = getString(R.string.logout_confirmation_message),
                    positive = getString(R.string.cancel),
                    negative = getString(R.string.yes_logout),
                    negativeClick = {
                        preferencesHelper.clear()
                        UtilityActions.updateFCM(preferencesHelper)

                        val intent = Intent(this@ActivityDashboard, ActivityLogin::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                ).show()
            }

            drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
                override fun onDrawerStateChanged(newState: Int) {}
                override fun onDrawerClosed(drawerView: View) {}

                override fun onDrawerOpened(drawerView: View) {
                    UtilityActions.closeKeyboard(this@ActivityDashboard, viewDataBinding.root)
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getHomePage(preferencesHelper.uid)
            }
        }
    }
}