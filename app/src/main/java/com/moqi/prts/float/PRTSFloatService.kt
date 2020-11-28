package com.moqi.prts.float

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import com.moqi.prts.R
import com.moqi.prts.access.GlobalStatus
import kotlinx.android.synthetic.main.view_prts_float.view.*

class PRTSFloatService : Service() {

    private val PRTS_NOTIFICATION_ID = 114514

    private lateinit var mProjectionManager: MediaProjectionManager
    private lateinit var mWindowManager: WindowManager
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var windowLayoutParams: WindowManager.LayoutParams

    private var mediaProjection: MediaProjection? = null
    private var imageReader: ImageReader? = null

    private var mFloatView: View? = null
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var isShowing = false


    private val handler = Handler()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("asdfg", "FloatService onCreate")
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mProjectionManager =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createFloatView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bindNotification()
        }
    }

    // todo:// 封装View对象，独立出去
    @SuppressLint("ClickableViewAccessibility")
    private fun createFloatView() {
        mFloatView =
            LayoutInflater.from(applicationContext).inflate(R.layout.view_prts_float, null, false)
        mFloatView?.apply {
            float_tv_prts.setOnClickListener {
//                stopSelf()
            }
            float_iv_prts.setOnTouchListener { v, event ->
                when(event.actionMasked){
                    MotionEvent.ACTION_DOWN -> {
                        mTouchX = event.x
                        mTouchY = event.y
                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_MOVE -> {
//                        val dx = event.x - mTouchX
//                        val dy = event.y - mTouchY
//                        Log.e("asdfg", "ACTION_MOVE float view to ${event.x}, ${event.y}")
                        moveViewTo(event.rawX - mTouchX, event.rawY - mTouchY - GlobalStatus.statusBarHeight)
                    }
                    MotionEvent.ACTION_UP -> {
//                        Log.e("asdfg", "ACTION_UP float view to ${event.x}, ${event.y}")
                        moveViewTo(0f, event.rawY - mTouchY - GlobalStatus.statusBarHeight)
                    }
                }
                return@setOnTouchListener false
            }
        }
    }

    private fun moveViewTo(x: Float, y: Float){
//        Log.e("asdfg", "move float view to $x, $y")
        windowLayoutParams.apply {
            this.x = x.toInt()
            this.y = y.toInt()
        }
        mWindowManager.updateViewLayout(mFloatView, windowLayoutParams)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("asdfg", "FloatService onStartCommand")
        intent?.apply {
            val showFloatView = getBooleanExtra("float", false)
            if (showFloatView && !isShowing) {
                showFloatView()
            }

            val startMediaProjection = getBooleanExtra("capture", false)
            if (startMediaProjection) {
                startMediaProjection(this)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isShowing) {
            mWindowManager.removeView(mFloatView)
        }
        mediaProjection?.stop()
    }

    private fun showFloatView() {
        isShowing = true
        windowLayoutParams = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
//            gravity = Gravity.START or Gravity.TOP
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            format = PixelFormat.RGBA_8888
//            x = 0
//            y = 0
            gravity = Gravity.START or Gravity.TOP
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        mWindowManager.addView(mFloatView, windowLayoutParams)
    }

    private fun startMediaProjection(intent: Intent) {
        imageReader = ImageReader.newInstance(
            GlobalStatus.screenHeight,
            GlobalStatus.screenWidth,
            PixelFormat.RGBA_8888,
            2
        ).apply {
//            setOnImageAvailableListener({
//                val image = it.acquireNextImage()
//                if (image != null) {
//                    val info = image.planes
//                    Log.e("asdfg", "$info")
//                    image.close()
//                }
//            }, null)
        }
        mediaProjection = mProjectionManager.getMediaProjection(Activity.RESULT_OK, intent)
        mediaProjection!!.createVirtualDisplay(
            "test", GlobalStatus.screenHeight, GlobalStatus.screenWidth,
            1, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader?.surface, null, null
        )

        watching()
    }

    private fun watching(){
        handler.postDelayed({
            val im = imageReader?.acquireLatestImage()
            im?.let {
                handleImage(it)
                it.close()
            }
            watching()
        }, 5000)
    }

    private fun handleImage(im: Image){
        // 1.判断是否在明日方舟内
        // 2.判断Image是在哪一个页面
        // 3.确定页面对应的可操作区域，进行模拟操作
        // 4.这些操作能在主线程做吗
        logImageInfo(im)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindNotification() {
        val nc = NotificationChannel("prts", "PRTS代理作战提示", NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(nc)
        val notification = Notification.Builder(applicationContext, nc.id)
            .setSmallIcon(R.mipmap.ic_kkdy)
            .setContentTitle("PRTS代理指挥中")
            .setContentText("正在持续获取战场信息")
            .setWhen(System.currentTimeMillis())
            .build()
        startForeground(PRTS_NOTIFICATION_ID, notification)
    }

    private fun logImageInfo(image: Image){
        Log.e("asdfg", "format=${image.format}, size=(${image.width}, ${image.height}), rs=${image.planes[0].rowStride}, ps=${image.planes[0].pixelStride}")
    }
}