package com.simplation.demo

import android.os.Bundle
import android.os.Environment
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.simplation.demo.databinding.ActivityMultiOpenGlplayerBinding

class MultiOpenGLPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMultiOpenGlplayerBinding

    private val path = Environment.getExternalStorageDirectory().absolutePath + "/mvtest.mp4"
    private val path2 = Environment.getExternalStorageDirectory().absolutePath + "/mvtest_2.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiOpenGlplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}