package hu.kole.daypicker

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import java.util.*

object Utils {
    @JvmStatic
    fun getDefaultLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }
    @JvmStatic
    fun getScreenWidth(context: Context) : Int {
        val displayMetrics = DisplayMetrics()
        if (context is Activity) {
            context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }

        return displayMetrics.widthPixels
    }
}