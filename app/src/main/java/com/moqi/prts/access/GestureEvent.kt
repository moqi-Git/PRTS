package com.moqi.prts.access

/**
 *
 * created by reol at 2020/12/4
 */
data class GestureEvent(
    val type: Type,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
){
    enum class Type{
        GESTURE_CLICK,
        GESTURE_SCROLL
    }
}

