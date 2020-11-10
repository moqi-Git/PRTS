package com.moqi.prts.access

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.moqi.prts.ext.getWindowSize

object GlobalStatus {

    var isPRTSConnected = false
    var currentForegroundApp = ""
    var hasCapturePermission = false

    var screenWidth = 0
    var screenHeight = 0

    var livePRTSConnected = MutableLiveData<Boolean>()


    fun setGlobalScreenSize(act: Activity){
        val size = act.getWindowSize()
        if (size.width > size.height){
            screenWidth = size.width
            screenHeight = size.height
        } else {
            screenHeight = size.width
            screenWidth = size.height
        }
    }
}
