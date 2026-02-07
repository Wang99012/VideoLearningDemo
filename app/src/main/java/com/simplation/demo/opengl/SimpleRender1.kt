package com.simplation.demo.opengl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.simplation.demo.opengl.drawer.IDrawer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SimpleRender1(private val mDrawer: IDrawer) : GLSurfaceView.Renderer {
    override fun onDrawFrame(gl: GL10?) {
        mDrawer.draw()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

    }

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?
    ) {
        //实现清屏，清屏颜色为黑色
        GLES20.glClearColor(0F, 0F, 0F, 0F)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        //创建了一个纹理ID，并设置给Drawer
        mDrawer.setTextureId(OpenGLTools.createTextureIds(1)[0])
    }
}