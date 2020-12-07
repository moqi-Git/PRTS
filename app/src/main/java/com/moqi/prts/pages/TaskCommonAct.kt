package com.moqi.prts.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moqi.prts.databinding.ActivitySampleBinding

class TaskCommonAct: AppCompatActivity() {

    private lateinit var vb: ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.tvSample.text = "TaskCommonAct"
    }
}