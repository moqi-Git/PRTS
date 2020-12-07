package com.moqi.prts.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moqi.prts.R
import com.moqi.prts.access.PRTSAccessibilityService
import com.moqi.prts.access.naviToSettingAccessibility
import com.moqi.prts.databinding.FragmentSlideshowBinding

class AutoReplyFragment : Fragment() {

  private lateinit var slideshowViewModel: AutoReplyViewModel

  private var vb: FragmentSlideshowBinding? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    slideshowViewModel = ViewModelProvider(this).get(AutoReplyViewModel::class.java)
    vb = FragmentSlideshowBinding.inflate(inflater, container, false)
    return vb?.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    vb?.wechatBtnStart?.setOnClickListener {
//      requireContext().naviToSettingAccessibility()
      requireContext().startService(Intent(requireContext(), PRTSAccessibilityService::class.java).apply {
        putExtra("event", "click")
      })
    }
  }
}