package com.aajeevika.clf.utility

import android.content.Context
import android.content.res.Configuration
import com.aajeevika.clf.utility.app_enum.LanguageType
import java.util.*

object LocaleHelper {
    fun updateLanguage(context: Context?, language: String?) : Context? {
        if(context != null) {
            val locale = Locale(language ?: LanguageType.HINDI.code)
            Locale.setDefault(locale)

            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(locale)

            return context.createConfigurationContext(configuration)
        }
        return context
    }
}