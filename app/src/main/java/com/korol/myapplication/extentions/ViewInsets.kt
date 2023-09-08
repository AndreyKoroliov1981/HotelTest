package com.korol.myapplication.extentions

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.addInsets(addStatusBarInset: Boolean = true, addNavigationBarInset: Boolean = true) {
    val bottomllContainer = this.paddingBottom
    val leftllContainer = this.paddingLeft
    val rightllContainer = this.paddingRight
    ViewCompat.setOnApplyWindowInsetsListener(this) { viewContainer, insets ->
        val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
        val navigationBarsBottom =
            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        val navigationBarsLeft = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).left
        val navigationBarsRight =
            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).right
        if (addStatusBarInset) viewContainer.updatePadding(top = statusBars)
        if (addNavigationBarInset) viewContainer.updatePadding(bottom = navigationBarsBottom + bottomllContainer)
        viewContainer.updatePadding(left = navigationBarsLeft + leftllContainer)
        viewContainer.updatePadding(right = navigationBarsRight + rightllContainer)
        WindowInsetsCompat.CONSUMED
    }
}

fun Activity.setColorStatusBar(view: View, color: Int? = null, darkModeIcon: Boolean = false) {
    val window: Window = this.window
    var colorBackground: Int
    if (color == null) {
        colorBackground = Color.TRANSPARENT
        val background: Drawable? = view.background
        if (background is ColorDrawable) {
            colorBackground =
                background.color
        }
    } else {
        colorBackground = ContextCompat.getColor(this, color)
    }

    val statusBarColor: Int = colorBackground
    if (statusBarColor == Color.BLACK && window.navigationBarColor == Color.BLACK) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
    window.statusBarColor = statusBarColor

    val versionSDK = Build.VERSION.SDK_INT

    @RequiresApi(Build.VERSION_CODES.R)
    when (versionSDK) {
        in 23..29 -> {
            var flags = window.decorView.systemUiVisibility
            if (darkModeIcon) {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            window.decorView.systemUiVisibility = flags
        }

        in 30..33 -> {
            window.setDecorFitsSystemWindows(false)
            if (darkModeIcon) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                )
            } else {
                window.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                )
            }
        }
    }
}