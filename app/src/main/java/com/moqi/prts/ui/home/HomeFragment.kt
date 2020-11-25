package com.moqi.prts.ui.home

import android.app.Activity.RESULT_OK
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.moqi.prts.R
import com.moqi.prts.access.GlobalStatus
import com.moqi.prts.ext.isServiceRunning
import com.moqi.prts.float.PRTSFloatService
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var mm: MediaProjectionManager
  private lateinit var holder: SurfaceHolder

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_home, container, false)
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    src_tv_start.setOnClickListener {
      if (requireContext().isServiceRunning("com.moqi.prts.float.PRTSFloatService")){
        return@setOnClickListener
      }
      mm = requireContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
      startActivityForResult(mm.createScreenCaptureIntent(), 101)
    }
//    holder = scr_sv_main.holder
  }

  private fun screenShot(image: Image){
//    logImageInfo(image)
    val buffer = image.planes[0].buffer
    val width = image.width
    val height = image.height
//    val pixelStride = image.planes[0].pixelStride
//    val rowStride = image.planes[0].rowStride
//    val rowPadding = rowStride - pixelStride * width

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.copyPixelsFromBuffer(buffer)
//    val bytes = ByteArray(buffer.capacity())
//    buffer.get(bytes)
//    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    scr_iv_main.setImageBitmap(bitmap)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 101 && resultCode == RESULT_OK) {
      data?.putExtra("capture", true)
      data?.putExtra("float", true)
      data?.setClass(requireContext(), PRTSFloatService::class.java)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        requireContext().startForegroundService(data)
      } else {
        requireContext().startService(data)
      }
    }
  }
}