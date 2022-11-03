package com.aajeevika.clf

import android.app.Application
import android.content.Context
import com.aajeevika.clf.koin.*
import com.aajeevika.clf.location.koin.locationNetworkModule
import com.aajeevika.clf.location.koin.locationViewModelModule
import com.aajeevika.clf.utility.AppPreferencesHelper
import com.aajeevika.clf.utility.LocaleHelper
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.android.ext.android.get

class MyApp : Application() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleHelper.updateLanguage(
                newBase,
                AppPreferencesHelper(newBase.getSharedPreferences(
                    "${BuildConfig.APPLICATION_ID}_app",
                    Context.MODE_PRIVATE
                )).appLanguage
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(listOf(appModule, networkModule, myViewModel, locationNetworkModule, locationViewModelModule))
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful) {
                val preferences: AppPreferencesHelper = get()
                preferences.fcmToken = it.result ?: ""
            }
        }
    }
}