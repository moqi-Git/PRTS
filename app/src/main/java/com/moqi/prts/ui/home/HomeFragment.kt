package com.moqi.prts.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
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
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var mm: MediaProjectionManager
  private lateinit var holder: SurfaceHolder
  private lateinit var imageReader: ImageReader

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
      mm = requireContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
      startActivityForResult(mm.createScreenCaptureIntent(), 101)
    }
    imageReader = ImageReader.newInstance(GlobalStatus.screenHeight, GlobalStatus.screenWidth, PixelFormat.RGBA_8888, 2).apply {
      setOnImageAvailableListener({
        Log.e("asdfg", "OnImageAvailable")
        val image = it.acquireNextImage()
        if (image != null){
          val info = image.planes
          Log.e("asdfg", "${info}")
          image.close()
        }
      }, null)
    }
    holder = scr_sv_main.holder
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val projection = mm.getMediaProjection(resultCode, data?:Intent())

    projection.createVirtualDisplay("test", GlobalStatus.screenHeight, GlobalStatus.screenWidth,
    1, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, imageReader.surface, null, null)
  }
}