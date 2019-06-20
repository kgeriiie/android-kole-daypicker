package hu.kole.daypicker.extensions

import android.os.Build
import android.widget.TextView

@Suppress("DEPRECATION")
fun TextView.setStyle(styleRes: Int) {
    if (Build.VERSION.SDK_INT < 23) {
        setTextAppearance(context, styleRes)
    } else {
        setTextAppearance(styleRes)
    }
}