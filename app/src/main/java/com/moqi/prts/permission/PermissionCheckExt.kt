package com.moqi.prts.permission

import android.content.Context
import android.provider.Settings

fun Context.isAccessibilityOpen(): Boolean{
    return true
}

fun Context.isFloatOnOpen(): Boolean{
    return Settings.canDrawOverlays(this)
}