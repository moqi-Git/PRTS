package com.moqi.prts.access

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import android.graphics.Path
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.moqi.prts.float.PRTSFloatService
import kotlin.concurrent.thread


class PRTSAccessibilityService : AccessibilityService() {

    private val handler = Handler()
    private lateinit var pkgManager: PackageManager
    private var isThreadRunning = false
    private var isArknight = false

    private val areaList = ArrayList<Area>()

    override fun onServiceConnected() {
        super.onServiceConnected()
        GlobalStatus.isPRTSConnected = true
//        EventBus.getDefault().register(this)
        Toast.makeText(applicationContext, "PRTS连接成功", Toast.LENGTH_SHORT).show()

        areaRangeList.forEach {
            areaList.add(
                Area(
                    it.ltx * GlobalStatus.screenWidth,
                    it.lty * GlobalStatus.screenHeight,
                    it.rbx * GlobalStatus.screenWidth,
                    it.rby * GlobalStatus.screenHeight
                )
            )
        }

        pkgManager = applicationContext.packageManager
    }

    override fun onUnbind(intent: Intent?): Boolean {
        GlobalStatus.isPRTSConnected = false
        handler.removeCallbacksAndMessages(null)
        isThreadRunning = false
//        EventBus.getDefault().unregister(this)
        Toast.makeText(applicationContext, "PRTS已断开", Toast.LENGTH_SHORT).show()
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("asdfg", "onStartCommand: event=${intent?.getStringExtra("event")}")
        handleGestureEvent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onInterrupt() {
        Log.e("asdfg", "onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.also {
//            GlobalStatus.currenstForegroundApp = event.packageName.toString()

            when (it.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    if (!isActivity(it)){
                        return
                    }
                    Log.e(
                        "asdfg",
                        "event: ${it.eventType}, text:${it.packageName}, ct:${it.className}"
                    )
                    if (event.packageName == "com.hypergryph.arknights") {
                        isArknight = true
                        arknights(it)
                        startService(
                            Intent(
                                applicationContext,
                                PRTSFloatService::class.java
                            ).apply { putExtra("float", true) })
                    } else {
                        if (isArknight) {
                            startService(
                                Intent(
                                    applicationContext,
                                    PRTSFloatService::class.java
                                ).apply { putExtra("float", false) })
                            Toast.makeText(applicationContext, "PRTS:已暂停", Toast.LENGTH_SHORT)
                                .show()
                        }
                        isArknight = false
                    }
                }
            }
        }
    }

    private fun arknights(event: AccessibilityEvent) {
        if (!isThreadRunning) {
            Toast.makeText(applicationContext, "PRTS:3秒后开始执行", Toast.LENGTH_SHORT).show()
            handler.postDelayed({
                // todo: 恢复的进度
                auto1_7()
            }, 3000)
        }
    }

    private fun isActivity(event: AccessibilityEvent): Boolean {
        val component = ComponentName(event.packageName.toString(), event.className.toString())
        return try {
            val info = pkgManager.getActivityInfo(component, PackageManager.GET_META_DATA)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
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
        isThreadRunning = true
        clickArea(areaList[0])
        handler.postDelayed({
            clickArea(areaList[1])
        }, 7000)
        handler.postDelayed({
            clickArea(areaList[2])
        }, 7000 + 11000)
        handler.postDelayed({
            auto1_7()
        }, 7000 + 11000 + 10000)
    }

    private fun handleGestureEvent(intent: Intent?){
        if (intent == null){
            return
        }
//        val gesture = intent.getParcelableExtra<GestureEvent>("event")
    }

    private fun clickPosition(px: Float, py: Float) {
        val path = Path()
        path.moveTo(px, py)
        path.lineTo(px, py)

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 10L, 34L))
            .build()

        dispatchGesture(gesture, object : GestureResultCallback() {
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

    private fun clickArea(area: Area) {
        val px = area.ltx + (area.rbx - area.ltx) * Math.random()
        val py = area.lty + (area.rby - area.lty) * Math.random()
        clickPosition(px.toFloat(), py.toFloat())
    }


    /**
     * 1305,637 0.858 0.884
     * 1463,683 0.963 0.949
     * 1170,380 0.769 0.527
     * 1282,634 0.843 0.880
     * 1209,171 0.795 0.237
     * 1429.322 0.940 0.447
     *
     * 1520,720
     */
    private val areaRangeList = arrayListOf(
        Area(0.858f, 0.884f, 0.963f, 0.949f),
        Area(0.769f, 0.527f, 0.843f, 0.880f),
        Area(0.795f, 0.237f, 0.940f, 0.447f)
    )
}

data class Area(
    val ltx: Float,
    val lty: Float,
    val rbx: Float,
    val rby: Float
)