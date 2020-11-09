package com.moqi.prts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import com.moqi.prts.access.GlobalStatus
import com.moqi.prts.access.naviToSettingAccessibility
import com.moqi.prts.permission.requestCapturePermission
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

//        requestCapturePermission(1002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002 && resultCode == RESULT_OK){
            GlobalStatus.hasCapturePermission = true
        }
    }

}