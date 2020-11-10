package com.moqi.prts.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moqi.prts.R

class AutoReplyFragment : Fragment() {

  private lateinit var slideshowViewModel: AutoReplyViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    slideshowViewModel = ViewModelProvider(this).get(AutoReplyViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
    return root
  }
}