package com.moqi.prts.access

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class WeChatAutoReplyService  : AccessibilityService(){

    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onUnbind(intent: Intent?): Boolean {
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
                        else -> {

                        }
                    }
                }
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                    it.text.forEach { cs ->
                        Log.e("asdfg", "notification content = $cs")
                        if (cs.startsWith("ƃuopoɐɥ:")){
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

    }
}