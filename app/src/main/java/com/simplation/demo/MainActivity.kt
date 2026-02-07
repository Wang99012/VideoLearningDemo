package com.simplation.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.PermissionLists
import com.simplation.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermissions()
        setupViews()
    }

    private fun setupPermissions() {
        XXPermissions.with(this)
            .permission(PermissionLists.getManageExternalStoragePermission())
            .request { _, deniedList ->
                val allGranted = deniedList.isEmpty()
                if (!allGranted) {
                    Toast.makeText(this@MainActivity, "需要权限才能读取本地文件", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setupViews() {
        binding.apply {
            //简单播放器
            btnSimplePlayer.setOnClickListener {
                launch<SimplePlayerActivity>()
            }

            //OpenGL 渲染简单三角形
            btnSimpleTriangle.setOnClickListener {
                launch<SimpleRenderActivity>(
                    "type" to 0
                )
            }
            btnSimpleTexture.setOnClickListener {
                launch<SimpleRenderActivity>(
                    "type" to 1
                )
            }
            btnOpenGLPlayer.setOnClickListener {
                launch<OpenGLPlayerActivity>()
            }

            btnMultiOpenGLPlayer.setOnClickListener {

            }

            btnEGLPlayer.setOnClickListener {

            }

            btnSoulPlayer.setOnClickListener {

            }

            btnEncoder.setOnClickListener {

            }

            btnFFmpegInfo.setOnClickListener {

            }

            btnFFmpegGLPlayer.setOnClickListener {

            }

            btnFFmpegRepack.setOnClickListener {

            }

            btnFFmpegEncode.setOnClickListener {

            }
        }
    }

}