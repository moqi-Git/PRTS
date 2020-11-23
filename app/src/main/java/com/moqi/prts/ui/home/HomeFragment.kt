package com.moqi.prts.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
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
import com.moqi.prts.float.PRTSFloatService
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var mm: MediaProjectionManager
  private lateinit var holder: SurfaceHolder
  private lateinit var imageReader: ImageReader

  private var screenShot = false

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
    mm = requireContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    startActivityForResult(mm.createScreenCaptureIntent(), 101)

    src_tv_start.setOnClickListener {
//      Log.e("qwert", "screenshot click")
//      screenShot = true
      requireContext().startService(Intent(requireContext(), PRTSFloatService::class.java).apply {
        putExtra("float", true)
      })
    }
    imageReader = ImageReader.newInstance(GlobalStatus.screenHeight, GlobalStatus.screenWidth, PixelFormat.RGBA_8888, 2).apply {
      setOnImageAvailableListener({
//        Log.e("asdfg", "OnImageAvailable")
        val image = it.acquireNextImage()
        if (image != null){
          val info = image.planes
//          Log.e("asdfg", "${info}")
          if (screenShot){
            Log.e("qwert", "screenshot start")
            screenShot(image)
            Log.e("qwert", "screenshot end")
            screenShot = false
          }
          image.close()
        }
      }, null)
    }
//    holder = scr_sv_main.holder
  }

  private fun screenShot(image: Image){
    logImageInfo(image)
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
    val projection = mm.getMediaProjection(resultCode, data?:Intent())

    projection.createVirtualDisplay("test", GlobalStatus.screenHeight, GlobalStatus.screenWidth,
    1, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.surface, null, null)
  }

  private fun logImageInfo(image: Image){
    Log.e("asdfg", "format=${image.format}, size=(${image.width}, ${image.height}), rs=${image.planes[0].rowStride}, ps=${image.planes[0].pixelStride}")
  }
}