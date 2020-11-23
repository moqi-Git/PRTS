package com.moqi.prts.float

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import com.moqi.prts.R
import kotlinx.android.synthetic.main.view_prts_float.view.*

class PRTSFloatService: Service() {

    private lateinit var mWindowManager: WindowManager
    private var mFloatView: View? = null

    private var isShowing = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("asdfg", "FloatService onCreate")
        mWindowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mFloatView = LayoutInflater.from(applicationContext).inflate(R.layout.view_prts_float, null, false)
        mFloatView?.apply {
            float_tv_prts.setOnClickListener {
                stopSelf()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("asdfg", "FloatService onStartCommand")
        if (intent?.getBooleanExtra("float", false) == true){
            if (!isShowing){
                showFloatView()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isShowing) {
            mWindowManager.removeView(mFloatView)
        }
    }

    private fun showFloatView(){
        isShowing = true
        val lp = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
//            gravity = Gravity.START or Gravity.TOP
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            format = PixelFormat.RGBA_8888
//            x = 0
//            y = 0
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        mWindowManager.addView(mFloatView, lp)
    }

}