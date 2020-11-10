package com.moqi.prts.ext

import android.app.Activity
import android.graphics.Point
import android.util.Size

fun Activity.getWindowSize(): Size{
    val w = windowManager.defaultDisplay
    if (w != null){
        val p = Point()
        w.getSize(p)
        return Size(p.x, p.y)
    } else {
        return Size(0, 0)
    }
}