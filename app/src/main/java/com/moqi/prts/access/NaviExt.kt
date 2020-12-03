package com.moqi.prts.access

import android.content.Context
import android.content.Intent
import android.provider.Settings
import java.lang.Exception

fun Context.naviToApp(pkgName: String): Boolean{
    try {
        val pm = packageManager
//        val info = pm.getApplicationInfo(pkgName, 0)
        startActivity(pm.getLaunchIntentForPackage(pkgName))
        return true
    } catch (e: Exception){
        return false
    }
}

fun Context.naviToSettingAccessibility(){
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}