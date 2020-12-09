package com.moqi.prts.float

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
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
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs

class PRTSFloatService : Service() {

    private val PRTS_NOTIFICATION_ID = 114514

    private lateinit var mProjectionManager: MediaProjectionManager
    private lateinit var mWindowManager: WindowManager
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var windowLayoutParams: WindowManager.LayoutParams

    private var mediaProjection: MediaProjection? = null
    private var imageReader: ImageReader? = null
    private val mWorker = Executors.newSingleThreadExecutor()

    private var mFloatView: View? = null
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var mMoveStartX = 0f
    private var mMoveStartY = 0f
    private var mPercentX = 0f
    private var mPercentY = 0f
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
    private fun createFloatView() {
        mFloatView =
            LayoutInflater.from(applicationContext).inflate(R.layout.view_prts_float, null, false)
        mFloatView?.apply {
            val tv = findViewById<View>(R.id.float_tv_prts)
            tv.setOnClickListener {
                stopSelf()
            }
            findViewById<View>(R.id.float_iv_prts).setOnTouchListener { v, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        mTouchX = event.x
                        mTouchY = event.y
                        mMoveStartX = event.rawX
                        mMoveStartY = event.rawY
                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_MOVE -> {
//                        val dx = event.x - mTouchX
//                        val dy = event.y - mTouchY
//                        Log.e("asdfg", "ACTION_MOVE float view to ${event.x}, ${event.y}")
                        moveViewTo(
                            event.rawX - mTouchX,
                            event.rawY - mTouchY - GlobalStatus.statusBarHeight
                        )
                    }
                    MotionEvent.ACTION_UP -> {
//                        Log.e("asdfg", "ACTION_UP float view to ${event.x}, ${event.y}")

                        if (abs(event.rawY - mMoveStartX + event.rawX - mMoveStartY) < 2) {
                            v.performClick()
                        }
                        val edgeX = if (GlobalStatus.screenWidth / 2 > event.rawX) {
                            0f
                        } else {
                            GlobalStatus.screenWidth - mFloatView!!.width.toFloat()
                        }
                        moveViewTo(edgeX, event.rawY - mTouchY - GlobalStatus.statusBarHeight)
                    }
                }
                return@setOnTouchListener false
            }
            findViewById<View>(R.id.float_iv_prts).setOnClickListener {
                if (tv.scaleX == 0f) {
                    tv.visibility = View.VISIBLE
                    tv.scaleX = 1f
                } else {
                    tv.visibility = View.GONE
                    tv.scaleX = 0f
                }

            }
        }
    }

    private fun moveViewTo(x: Float, y: Float) {
//        Log.e("asdfg", "move float view to $x, $y")
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPercentX = x / GlobalStatus.screenWidth
            mPercentY = y / GlobalStatus.screenHeight
        } else {
            mPercentX = x / GlobalStatus.screenHeight
            mPercentY = y / GlobalStatus.screenWidth
        }
        windowLayoutParams.apply {
            this.x = x.toInt()
            this.y = y.toInt()
        }
        mWindowManager.updateViewLayout(mFloatView, windowLayoutParams)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.apply {
            val showFloatView = getBooleanExtra("float", false)
            if (showFloatView && !isShowing) {
                showFloatView()
            }
            if (!showFloatView && isShowing) {
                isShowing = false
                mWindowManager?.removeView(mFloatView)
            }

            val startMediaProjection = getBooleanExtra("capture", false)
            if (startMediaProjection) {
                startMediaProjection(this)
            }

            Log.e(
                "asdfg",
                "FloatService onStartCommand: showFloatView=$showFloatView, startMediaProjection=$startMediaProjection"
            )
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isShowing) {
            mWindowManager.removeView(mFloatView)
        }
        handler.removeCallbacksAndMessages(null)
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
            flags =
//                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
//            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                WindowManager.LayoutParams.FLAG_SPLIT_TOUCH or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
//            flags = WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                    WindowManager.LayoutParams.FLAG_DIM_BEHIND or
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            dimAmount = 0.5f
            format = PixelFormat.RGBA_8888
            x = 0
            y = 0
            gravity = Gravity.START or Gravity.TOP
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        mWindowManager.addView(mFloatView, windowLayoutParams)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.e("asdfg", "onConfigurationChanged")
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            moveViewTo(mPercentX * GlobalStatus.screenHeight, mPercentY * GlobalStatus.screenWidth)
        } else {
            moveViewTo(mPercentX * GlobalStatus.screenWidth, mPercentY * GlobalStatus.screenHeight)
        }
    }

    @SuppressLint("WrongConstant")
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
            "test",
            GlobalStatus.screenHeight,
            GlobalStatus.screenWidth,
            1,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader?.surface,
            object : VirtualDisplay.Callback() {
                override fun onPaused() {
                    super.onPaused()
                    Log.e("asdfg", "VirtualDisplay callback onPaused")
                }

                override fun onResumed() {
                    super.onResumed()
                    Log.e("asdfg", "VirtualDisplay callback onResumed")
                }

                override fun onStopped() {
                    super.onStopped()
                    Log.e("asdfg", "VirtualDisplay callback onStopped")
                }
            },
            null
        )
        watching()
    }

    private fun watching() {
        handler.postDelayed({
            val im = imageReader?.acquireLatestImage()
            im?.let {
                handleImage(it)
            }
            watching()
        }, 5000)
    }

    private fun handleImage(im: Image) {
        // 1.判断是否在明日方舟内
        // 2.判断Image是在哪一个页面
        // 3.确定页面对应的可操作区域，进行模拟操作
        // 4.这些操作能在主线程做吗
        mWorker.execute {
            saveImage(im)
        }
        logImageInfo(im)
    }

    private fun saveImage(image: Image) {
        val savePath = applicationContext.getExternalFilesDir("ptilopsis")
        val saveName = "im${System.currentTimeMillis()}.jpg"

        val buffer = image.planes[0].buffer
        val height = image.height
        val pixelStride = image.planes[0].pixelStride
        val rowStride = image.planes[0].rowStride

        val bitmap = Bitmap.createBitmap(rowStride / pixelStride, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        image.close()

        try {
            val file = File(savePath, saveName)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {

        }
    }

//    private fun screenShot(image: Image){
////    logImageInfo(image)
//        val buffer = image.planes[0].buffer
//        val width = image.width
//        val height = image.height
////    val pixelStride = image.planes[0].pixelStride
////    val rowStride = image.planes[0].rowStride
////    val rowPadding = rowStride - pixelStride * width
//
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        bitmap.copyPixelsFromBuffer(buffer)
////    val bytes = ByteArray(buffer.capacity())
////    buffer.get(bytes)
////    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//    }


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

    private fun logImageInfo(image: Image) {
        Log.e(
            "asdfg",
            "format=${image.format}, size=(${image.width}, ${image.height}), rs=${image.planes[0].rowStride}, ps=${image.planes[0].pixelStride}"
        )
    }
}