package com.moqi.prts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import com.moqi.prts.access.GlobalStatus
import com.moqi.prts.access.naviToSettingAccessibility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_btn_get_permission.setOnClickListener {
            if (!GlobalStatus.isPRTSConnected){
                naviToSettingAccessibility()
            } else {
                Toast.makeText(this, "PRTS正常运行中", Toast.LENGTH_SHORT).show()
            }
        }
    }

}