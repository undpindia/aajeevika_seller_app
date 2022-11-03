package com.aajeevika.clf.utility

import android.content.SharedPreferences
import com.aajeevika.clf.utility.app_enum.LanguageType

class AppPreferencesHelper(private val mPrefs: SharedPreferences) {
    var uid: Int
        get() = mPrefs.getInt("uid", -1)
        set(value) = mPrefs.edit().putInt("uid", value).apply()

    var roleId: Int
        get() = mPrefs.getInt("roleId", -1)
        set(value) = mPrefs.edit().putInt("roleId", value).apply()

    var name: String
        get() = mPrefs.getString("name", "") ?: ""
        set(value) = mPrefs.edit().putString("name", value).apply()

    var memberId: String
        get() = mPrefs.getString("memberId", "") ?: ""
        set(value) = mPrefs.edit().putString("memberId", value).apply()

    var organisationName: String
        get() = mPrefs.getString("organisationName", "") ?: ""
        set(value) = mPrefs.edit().putString("organisationName", value).apply()

    var memberDesignation: String
        get() = mPrefs.getString("memberDesignation", "") ?: ""
        set(value) = mPrefs.edit().putString("memberDesignation", value).apply()

    var address: String
        get() = mPrefs.getString("address", "") ?: ""
        set(value) = mPrefs.edit().putString("address", value).apply()

    var title: String
        get() = mPrefs.getString("title", "") ?: ""
        set(value) = mPrefs.edit().putString("title", value).apply()

    var email: String
        get() = mPrefs.getString("email", "") ?: ""
        set(value) = mPrefs.edit().putString("email", value).apply()

    var mobile: String
        get() = mPrefs.getString("mobile", "") ?: ""
        set(value) = mPrefs.edit().putString("mobile", value).apply()

    var profileImage: String
        get() = mPrefs.getString("profileImage", "") ?: ""
        set(value) = mPrefs.edit().putString("profileImage", value).apply()

    var authToken: String
        get() = mPrefs.getString("authToken", "").toString()
        set(value) = mPrefs.edit().putString("authToken", value).apply()

    var fcmToken: String
        get() = mPrefs.getString("fcmToken", "").toString()
        set(value) = mPrefs.edit().putString("fcmToken", value).apply()

    var languageSelected: Boolean
        get() = mPrefs.getBoolean("languageSelected", false)
        set(value) = mPrefs.edit().putBoolean("languageSelected", value).apply()

    var appLanguage: String
        get() = mPrefs.getString("appLanguage", LanguageType.ENGLISH.code).toString()
        set(value) = mPrefs.edit().putString("appLanguage", value).apply()

    fun clear() {
        val token = fcmToken
        val selectedLanguage = appLanguage

        mPrefs.edit().clear().apply()

        appLanguage = selectedLanguage
        fcmToken = token
    }
}