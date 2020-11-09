package com.moqi.prts.access

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Path
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import java.lang.Exception
import kotlin.concurrent.thread


class PRTSAccessibilityService : AccessibilityService() {

    private val handler = Handler()
    private var isThreadRunning = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        GlobalStatus.isPRTSConnected = true
//        EventBus.getDefault().register(this)
        Toast.makeText(applicationContext, "PRTS连接成功", Toast.LENGTH_SHORT).show()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        GlobalStatus.isPRTSConnected = false
//        EventBus.getDefault().unregister(this)
        Toast.makeText(applicationContext, "PRTS已断开", Toast.LENGTH_SHORT).show()
        return super.onUnbind(intent)
    }

    override fun onInterrupt() {
        Log.e("asdfg", "onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.also {
            Log.e("asdfg", "event: ${it.eventType}")
//            val root = rootInActiveWindow ?: return
////            logNode(root)

            when (it.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    arknights(it)
                }
            }
        }
    }

    private fun arknights(event: AccessibilityEvent) {
        Log.e("asdfg", "foreground app changed to ${event.packageName}")
        GlobalStatus.currentForegroundApp = event.packageName.toString()

        if (event.packageName == "com.hypergryph.arknights" && !isThreadRunning) {
            handler.postDelayed({
                Toast.makeText(applicationContext, "PRTS:3秒后开始执行", Toast.LENGTH_SHORT).show()
                auto1_7()
            }, 3000)
        }
    }

    private fun logNode(node: AccessibilityNodeInfo) {
        Log.e(
            "asdfg",
            "node is ${node.className} in ${node.packageName}, content = ${node.text} | ${node.contentDescription}"
        )
        if (node.childCount > 0) {
            for (i in 0 until node.childCount) {
                val n = node.getChild(i) ?: continue
                logNode(n)
            }
        }
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public fun auto1_7() {
        if (isThreadRunning) {
            return
        }
        thread {
            isThreadRunning = true
            while (true) {
                clickPosition(1370.0f, 655.0f)
                Thread.sleep(5000)
                clickPosition(1230.0f, 505.0f)
                Thread.sleep(95 * 1000)
                clickPosition(1201.0f, 503.0f)
                Thread.sleep(7000)
            }
        }
    }

    private fun clickPosition(px: Float, py: Float) {
        val path = Path()
        path.moveTo(px, py)
        path.lineTo(px, py)

        val desp = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 10L, 34L))
            .build()
        dispatchGesture(desp, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.e("asdfg", "click position ($px, $py) completed")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.e("asdfg", "click position ($px, $py) canceled")
            }
        }, null)
    }

    private fun scroll(fromX: Float, fromY: Float, toX: Float, toY: Float) {

    }

    private fun clickArea(area: Area){
        val px = area.ltx + (area.rbx - area.ltx) * Math.random()
        val py = area.lty + (area.rby - area.lty) * Math.random()
        clickPosition(px.toFloat(), py.toFloat())
    }

}

data class Area(
    val ltx: Float,
    val lty: Float,
    val rbx: Float,
    val rby: Float
)