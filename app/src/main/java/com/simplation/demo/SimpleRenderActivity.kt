package com.simplation.demo

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simplation.demo.databinding.ActivitySimpleRenderBinding
import com.simplation.demo.opengl.SimpleRender1
import com.simplation.demo.opengl.drawer.BitmapDrawer
import com.simplation.demo.opengl.drawer.IDrawer
import com.simplation.demo.opengl.drawer.TriangleDrawer

class SimpleRenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySimpleRenderBinding
    private lateinit var drawer: IDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleRenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawer = (if (intent.getIntExtra("type", 0) == 0) {
            TriangleDrawer()
        } else {
            BitmapDrawer(BitmapFactory.decodeResource(resources, R.drawable.cover))
        })
        initRender(drawer)
    }

    private fun initRender(drawer: IDrawer) {
        binding.apply {
            glSurface.setEGLContextClientVersion(2)
            glSurface.setRenderer(SimpleRender1(drawer))
        }
    }

    override fun onDestroy() {
        drawer.release()
        super.onDestroy()
    }
}