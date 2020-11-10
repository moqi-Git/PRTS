package com.moqi.prts.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moqi.prts.R
import com.moqi.prts.access.GlobalStatus
import com.moqi.prts.access.naviToSettingAccessibility
import com.moqi.prts.ext.getWindowSize
import kotlinx.android.synthetic.main.fragment_gallery.*

class PRTSFragment : Fragment() {

  private lateinit var galleryViewModel: PRTSViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    galleryViewModel = ViewModelProvider(this).get(PRTSViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_gallery, container, false)
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initView()
  }

  private fun initView(){
    prts_rv_main.layoutManager = LinearLayoutManager(requireContext())
    prts_rv_main.adapter = PRTSAdapter().apply {
      onItemClickEvent = { p ->
//        Toast.makeText(requireContext(), "$p", Toast.LENGTH_SHORT).show()
        when(p){
          0 -> {
            Toast.makeText(requireContext(), "开发中…", Toast.LENGTH_SHORT).show()
          }
          1 -> {
            //
            Toast.makeText(requireContext(), "screen width = ${GlobalStatus.screenWidth}, height = ${GlobalStatus.screenHeight}", Toast.LENGTH_SHORT).show()
          }
          2 -> {
            if (!GlobalStatus.isPRTSConnected){
              requireContext().naviToSettingAccessibility()
            } else {
              Toast.makeText(requireContext(), "PRTS正常运行中", Toast.LENGTH_SHORT).show()
            }
          }
        }
      }
    }
  }



}