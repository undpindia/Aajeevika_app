package com.aajeevika.individual.view.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivityVM
import com.aajeevika.individual.databinding.ActivitySplashScreenBinding
import com.aajeevika.individual.view.auth.viewmodel.SplashScreenViewModel
import com.aajeevika.individual.view.home.ActivityDashboard

class ActivitySplashScreen : BaseActivityVM<ActivitySplashScreenBinding, SplashScreenViewModel>(
    R.layout.activity_splash_screen,
    SplashScreenViewModel::class
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        if(preferencesHelper.authToken.isNotEmpty()) {
            viewModel.requestUserProfile()
        } else if(!preferencesHelper.languageSelected) {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@ActivitySplashScreen, ActivitySelectLanguage::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@ActivitySplashScreen, ActivityLogin::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.userLiveData.observe(this, {
            it.user?.run {
                preferencesHelper.uid = id ?: -1
                preferencesHelper.name = name ?: ""
                preferencesHelper.email = email ?: ""
                preferencesHelper.mobile = mobile ?: ""
                preferencesHelper.profileImage = profileImage ?: ""
                preferencesHelper.roleId = role_id ?: -1
            }

            if (it.is_otp_verified == 0 || it.user?.is_document_added == 0 || it.user?.selectInterest == false) {
                preferencesHelper.clear()

                val intent = Intent(this@ActivitySplashScreen, ActivityLogin::class.java)
                startActivity(intent)
                finish()
            } else {
                it.user?.run {
                    preferencesHelper.uid = id ?: -1
                    preferencesHelper.name = name ?: ""
                    preferencesHelper.email = email ?: ""
                    preferencesHelper.mobile = mobile ?: ""
                    preferencesHelper.profileImage = profileImage ?: ""
                    preferencesHelper.roleId = role_id ?: -1
                }

                val intent = Intent(this, ActivityDashboard::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}