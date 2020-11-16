package com.moqi.prts.access

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.core.os.bundleOf

class WeChatAutoReplyService  : AccessibilityService(){

    private val handler = Handler()

    override fun onServiceConnected() {
        super.onServiceConnected()
        AccessibilityServiceInfo.FEEDBACK_SPOKEN

        Log.e("asdfg", "WeChatAutoReplyService#onServiceConnected")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("asdfg", "WeChatAutoReplyService#onUnbind")
        return super.onUnbind(intent)
    }

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.also {
            when (it.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    val desp = it.contentDescription
                    when(desp){
                        "当前所在页面,与微信团队的聊天" -> {

                        }
                        "当前所在页面,与ƃuopoɐɥ的聊天" -> {
                            Log.e("asdfg", "已开启聊天页面")
                            sendMsg(it, "what？")
                        }
                    }
                }
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                    it.text.forEach { cs ->
                        Log.e("asdfg", "notification content = $cs")
                        if (cs.contains(":")){
                            // 身份验证通过：
                            openWechatByNotification(it)
                        } else {
                            // 非指定用户
                        }
                    }

                }
            }
        }
    }

    private fun openWechatByNotification(event: AccessibilityEvent){
        val data = event.parcelableData
        if (data is Notification){
            try {
                val intent = data.contentIntent
                intent.send()
            } catch (e: PendingIntent.CanceledException){
                Toast.makeText(applicationContext, "获取通知信息失败", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun sendMsg(event: AccessibilityEvent, msg: String){
        val nodeInfo = rootInActiveWindow
        val inputSuccess = inputText(nodeInfo, msg)
        if (inputSuccess){
            handler.postDelayed({pressSend()},1000)
        }
    }

    private fun inputText(nodeInfo: AccessibilityNodeInfo?, msg: String): Boolean{
        if (nodeInfo == null){
            return false
        }
        Log.e("asdfg", "input searching, node class = ${nodeInfo.className}, text = ${nodeInfo.text}")
        if (nodeInfo.className == "android.widget.EditText"){
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundleOf(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE to msg
            ))
            return true
        }

        val count = nodeInfo.childCount
        for (i in 0 until count){
            if (inputText(nodeInfo.getChild(i), msg)){
                return true
            }
        }
        return false
    }



    private fun pressSend() {
        val nodeInfo = rootInActiveWindow
        if (nodeInfo != null) {
            val list = nodeInfo.findAccessibilityNodeInfosByText("发送")
            if (list != null) {
                for (n in list) {
                    if(n.className == "android.widget.Button" && n.isEnabled) {
                        n.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                }
            }
        }

        handler.postDelayed({performGlobalAction(GLOBAL_ACTION_HOME)}, 500)
    }

}