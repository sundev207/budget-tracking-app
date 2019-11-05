package com.sundev207.expenses.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sundev207.expenses.Application
import com.sundev207.expenses.R
import com.sundev207.expenses.authentication.AuthenticationManager
import com.sundev207.expenses.data.preference.PreferenceDataSource
import com.sundev207.expenses.home.presentation.HomeActivity
import com.sundev207.expenses.onboarding.OnboardingActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (isUserOnboarded()) {
            HomeActivity.start(this)
        } else {
            OnboardingActivity.start(this)
        }

        finish()
    }

    private fun isUserOnboarded(): Boolean {
        val preferenceDataSource = PreferenceDataSource()
        return preferenceDataSource.getIsUserOnboarded(applicationContext)
    }
}