package com.simplation.demo

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.simplation.demo.databinding.ActivitySimplePlayerBinding
import com.simplation.demo.media.decoder.AudioDecoder
import com.simplation.demo.media.decoder.VideoDecoder
import com.simplation.demo.media.muxer.Mp4Repack
import java.io.File
import java.util.concurrent.Executors

class SimplePlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySimplePlayerBinding

    lateinit var audioDecoder: AudioDecoder
    lateinit var videoDecoder: VideoDecoder


    val path = Environment.getExternalStorageDirectory().absolutePath + "/mvtest.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimplePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        initPlayer()
    }

    private fun setupViews() {
        binding.btnRepack.setOnClickListener {
            repack()
        }
    }

    private fun repack() {
        val repack = Mp4Repack(path = path)
        repack.start()
    }

    private fun initPlayer() {
        val threadPool = Executors.newFixedThreadPool(10)

        //创建视频解码器
        videoDecoder = VideoDecoder(path, binding.sfv, null)
        threadPool.execute(videoDecoder)

        //创建音频解码器
        audioDecoder = AudioDecoder(path)
        threadPool.execute(audioDecoder)

        //播放
        videoDecoder.goOn()
        audioDecoder.goOn()
    }

    override fun onDestroy() {
        videoDecoder.stop()
        audioDecoder.stop()
        super.onDestroy()
    }
}