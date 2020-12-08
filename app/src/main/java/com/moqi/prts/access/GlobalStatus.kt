package com.moqi.prts.access

import android.app.Activity
import android.util.Log
import com.moqi.prts.ext.getWindowSize


object GlobalStatus {

    var isPRTSConnected = false
    var currentForegroundApp = ""
    var hasCapturePermission = false

    var screenWidth = 0
    var screenHeight = 0
    var statusBarHeight = 0

    fun setGlobalScreenSize(act: Activity){
        val size = act.getWindowSize()
        if (size.width < size.height){
            screenWidth = size.width
            screenHeight = size.height
        } else {
            screenHeight = size.width
            screenWidth = size.height
        }
        val resourceId: Int = act.resources
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = act.resources.getDimensionPixelSize(resourceId)
        }
        Log.e("asdfg", "scw=$screenWidth, sch=$screenHeight, sBH=$statusBarHeight")
    }
}
