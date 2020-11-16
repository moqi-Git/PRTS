package com.moqi.prts.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PRTSViewModel : ViewModel() {
    val data = MutableLiveData<String>("asd")
}