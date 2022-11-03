package com.aajeevika.individual.koin

import android.content.Context
import com.aajeevika.individual.BuildConfig
import com.aajeevika.individual.utility.AppPreferencesHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        androidContext().getSharedPreferences("${BuildConfig.APPLICATION_ID}_app", Context.MODE_PRIVATE)
    }

    single {
        AppPreferencesHelper(get())
    }
}