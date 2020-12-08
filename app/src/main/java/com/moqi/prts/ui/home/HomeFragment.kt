package com.moqi.prts.ui.home

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.Image
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moqi.prts.databinding.FragmentHomeBinding
import com.moqi.prts.ext.isServiceRunning
import com.moqi.prts.float.PRTSFloatService

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var mediaProjectionManager: MediaProjectionManager
  private lateinit var holder: SurfaceHolder

  private var vb: FragmentHomeBinding? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//    val root = inflater.inflate(R.layout.fragment_home, container, false)
    vb = FragmentHomeBinding.inflate(inflater, container, false)
    return vb?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    vb?.srcTvStart?.setOnClickListener {
      if (requireContext().isServiceRunning("com.moqi.prts.float.PRTSFloatService")){
        return@setOnClickListener
      }
      mediaProjectionManager = requireContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
      startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), 101)
    }
//    holder = scr_sv_main.holder
  }

  override fun onDestroyView() {
    super.onDestroyView()
    vb = null
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