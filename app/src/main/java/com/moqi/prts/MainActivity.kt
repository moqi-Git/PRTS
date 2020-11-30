package com.moqi.prts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.moqi.prts.access.GlobalStatus
import com.moqi.prts.access.naviToSettingAccessibility
import com.moqi.prts.databinding.ActivityMainBinding
import com.moqi.prts.permission.requestCapturePermission

class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.mainBtnGetPermission.setOnClickListener {
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