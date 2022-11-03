package com.aajeevika.clf.koin

import android.content.Context
import com.aajeevika.clf.BuildConfig
import com.aajeevika.clf.utility.AppPreferencesHelper
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