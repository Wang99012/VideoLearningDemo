package com.simplation.demo.opengl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.simplation.demo.opengl.drawer.IDrawer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SimpleRender : GLSurfaceView.Renderer {
    private val drawers = mutableListOf<IDrawer>()

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BITS)
        drawers.forEach {
            it.draw()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        for (drawer in drawers) {
            drawer.setWorldSize(width, height)
        }
    }

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        //实现清屏，清屏颜色为黑色
        GLES20.glClearColor(0F, 0F, 0F, 0F)
        val textureIds = OpenGLTools.createTextureIds(drawers.size)
        for ((ids, drawer) in drawers.withIndex()) {
            drawer.setTextureId(textureIds[ids])
        }
    }

    fun addDrawer(drawer: IDrawer) {
        drawers.add(drawer)
    }
}