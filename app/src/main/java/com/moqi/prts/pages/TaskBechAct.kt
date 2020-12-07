package com.moqi.prts.pages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moqi.prts.databinding.ActivitySampleBinding

class TaskBechAct: AppCompatActivity() {

    private lateinit var vb: ActivitySampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.tvSample.text = "TaskBechAct"
        vb.tvSample.setOnClickListener {
            startActivity(Intent(this, TaskCommonAct::class.java))
        }
    }
}