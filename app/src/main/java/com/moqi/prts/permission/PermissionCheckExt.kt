package com.moqi.prts.permission

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.provider.Settings


fun Context.isAccessibilityOpen(): Boolean{
    return true
}

fun Context.isFloatOnOpen(): Boolean{
    return Settings.canDrawOverlays(this)
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.requestCapturePermission(requestCode: Int) {
    val mediaProjectionManager =
        getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?
    this.startActivityForResult(
        mediaProjectionManager!!.createScreenCaptureIntent(),
        requestCode
    )
}