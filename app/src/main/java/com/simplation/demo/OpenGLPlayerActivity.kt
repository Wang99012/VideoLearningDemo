package com.simplation.demo

import android.os.Bundle
import android.os.Environment
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import com.simplation.demo.databinding.ActivityOpenGlplayerBinding
import com.simplation.demo.media.decoder.AudioDecoder
import com.simplation.demo.media.decoder.VideoDecoder
import com.simplation.demo.opengl.SimpleRender1
import com.simplation.demo.opengl.drawer.IDrawer
import com.simplation.demo.opengl.drawer.VideoDrawer
import java.util.concurrent.Executors

/**
 * OpenGL 视频播放
 */
class OpenGLPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenGlplayerBinding
    private lateinit var drawer: IDrawer

    val path = Environment.getExternalStorageDirectory().absolutePath + "/mvtest.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenGlplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRender()
    }

    private fun initRender() {
        drawer = VideoDrawer()
        //设置视频的宽和高
        drawer.setVideoSize(1920, 1080)
        drawer.getSurfaceTexture {
            initPlayer(Surface(it))
        }

        binding.glSurfaceView.apply {
            setEGLContextClientVersion(2)
            setRenderer(SimpleRender1(drawer))
        }
    }

    private fun initPlayer(surface: Surface) {
        val threadPool = Executors.newFixedThreadPool(10)

        val videoDecoder = VideoDecoder(path, null, surface)

        threadPool.execute(videoDecoder)

        val audioDecoder = AudioDecoder(path)
        threadPool.execute(audioDecoder)

        videoDecoder.goOn()
        audioDecoder.goOn()
    }
}
