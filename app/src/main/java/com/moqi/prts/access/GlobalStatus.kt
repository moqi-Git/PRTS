package com.moqi.prts.access

import androidx.lifecycle.MutableLiveData

object GlobalStatus {

    var isPRTSConnected = false
    var currentForegroundApp = ""

    var livePRTSConnected = MutableLiveData<Boolean>()

}
