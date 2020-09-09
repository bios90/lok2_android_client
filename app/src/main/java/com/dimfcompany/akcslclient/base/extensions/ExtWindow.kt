package com.dimfcompany.akcslclient.base.extensions

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

fun Window.setStatusBarColorMy(color: Int)
{
    this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    this.setStatusBarColor(color)
}

fun Window.setStatusLightDark(is_light: Boolean)
{
    if (Build.VERSION.SDK_INT < 23)
    {
        return
    }

    var flags = this.decorView.systemUiVisibility
    if (is_light)
    {
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    else
    {
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    this.decorView.systemUiVisibility = flags
}

fun Window.setNavBarColor(color: Int)
{
    this.setNavigationBarColor(color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
    {
        this.setNavigationBarDividerColor(color)
    }
}

fun Window.setNavBarLightDark(is_light: Boolean)
{
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
    {
        return
    }

    var flags = this.decorView.systemUiVisibility
    if (is_light)
    {
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
    else
    {
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
    this.decorView.systemUiVisibility = flags
}

fun Window.makeFullScreen(below_nav_bar: Boolean = true)
{
    //!!!Do not forget to make android:fitsSystemWindows="false"root layout!!!
    if (below_nav_bar)
    {
        this.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
    else
    {
        this.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    }

    this.statusBarColor = Color.TRANSPARENT
    this.navigationBarColor = Color.TRANSPARENT
}
