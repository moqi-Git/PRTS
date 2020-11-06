package com.moqi.prts.access

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.concurrent.thread


class PRTSAccessibilityService : AccessibilityService() {

    private val handler = Handler()
    private var isThreadRunning = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        GlobalStatus.isPRTSConnected = true
//        EventBus.getDefault().register(this)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        GlobalStatus.isPRTSConnected = false
//        EventBus.getDefault().unregister(this)
        return super.onUnbind(intent)
    }

    override fun onInterrupt() {
        Log.e("asdfg", "onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            Log.e("asdfg", "event: ${it.eventType}")
            val root = rootInActiveWindow ?: return
            logNode(root)

            when (it.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    Log.e("asdfg", "foreground app changed to ${it.packageName}")
                    GlobalStatus.currentForegroundApp = it.packageName.toString()

                    if (it.packageName == "com.hypergryph.arknights") {
                        handler.postDelayed({ auto1_7() }, 3000)
                    }
                }

//                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
//                    if (root.packageName == "com.hypergryph.arknights") {
//                        Log.e("asdfg", "hhh")
//                        clickPosition(1370.0f, 656.0f)
//                    }
//                }
            }
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

}