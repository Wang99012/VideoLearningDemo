package com.simplation.demo.opengl.drawer

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * 绘制图片
 */
class BitmapDrawer(private val mBitmap: Bitmap) : IDrawer {
    //顶点坐标
    private val mVertexCoors = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f
    )

    //纹理坐标
    private val mTextureCoors = floatArrayOf(
        0f, 1f,
        1f, 1f,
        0f, 0f,
        1f, 0f
    )

    private var mTextureId: Int = -1
    private var mProgress: Int = -1

    private var mVertexPosHandler: Int = -1
    private var mTexturePosHandler: Int = -1
    private var mTextureHandler: Int = -1

    private lateinit var mVertexBuffer: FloatBuffer
    private lateinit var mTextureBuffer: FloatBuffer

    init {
        //【步骤1: 初始化顶点坐标】
        initPos()
    }

    private fun initPos() {
        val bb = ByteBuffer.allocateDirect(mVertexCoors.size * 4)
        bb.order(ByteOrder.nativeOrder())

        mVertexBuffer = bb.asFloatBuffer()
        mVertexBuffer.put(mVertexCoors)
        mVertexBuffer.position(0)

        val cc = ByteBuffer.allocateDirect(mTextureCoors.size * 4)
        cc.order(ByteOrder.nativeOrder())
        mTextureBuffer = cc.asFloatBuffer()
        mTextureBuffer.put(mTextureCoors)
        mTextureBuffer.position(0)
    }

    override fun draw() {
        if (mTextureId != -1) {
            //【步骤2: 创建、编译并启动OpenGL着色器】
            createGLPrg()
            //【步骤3: 激活并绑定纹理单元】
            activateTexture()
            //【步骤4: 绑定图片到纹理单元】
            bindBitmapToTexture()
            //【步骤5: 开始渲染绘制】
            doDraw()
        }
    }

    private fun doDraw() {
        GLES20.glEnableVertexAttribArray(mVertexPosHandler)
        GLES20.glEnableVertexAttribArray(mTexturePosHandler)

        GLES20.glVertexAttribPointer(mVertexPosHandler, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        GLES20.glVertexAttribPointer(mTexturePosHandler, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun bindBitmapToTexture() {
        if (!mBitmap.isRecycled) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0)
        }
    }

    private fun activateTexture() {
        //激活指定纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理ID到纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId)
        //将激活的纹理单元传递到着色器里面
        GLES20.glUniform1i(mTextureHandler, 0)
        //配置边缘过渡参数
        /**
         * 两个模式：GL_NEAREST（邻近过滤）、GL_LINEAR（线性过滤）
         * 当设置为GL_NEAREST的时候，OpenGL会选择中心点最接近纹理坐标的那个像素。
         *
         * 当设置为GL_LINEAR的时候，它会基于纹理坐标附近的纹理像素，计算出一个插值，近似出这些纹理像素之间的颜色。
         */
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
    }

    private fun createGLPrg() {
        if (mProgress == -1) {
            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, getVertexShader())
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, getFragmentShader())

            //创建OpenGL ES程序，注意：需要在OpenGL渲染线程中创建，否则无法渲染
            mProgress = GLES20.glCreateProgram()
            //将顶点着色器加入到程序中
            GLES20.glAttachShader(mProgress, vertexShader)
            //将片元着色器加入到程序中
            GLES20.glAttachShader(mProgress, fragmentShader)
            //连接到着色器程序
            GLES20.glLinkProgram(mProgress)

            //Java和GLSL交互的通道，通过属性可以给GLSL设置相关的值。
            mVertexPosHandler = GLES20.glGetAttribLocation(mProgress, "aPosition")
            mTexturePosHandler = GLES20.glGetAttribLocation(mProgress, "aCoordinate")
            mTextureHandler = GLES20.glGetUniformLocation(mProgress, "uTexture")

        }
        GLES20.glUseProgram(mProgress)
    }

    private fun getVertexShader(): String {
        return "attribute vec4 aPosition;" +
                "attribute vec2 aCoordinate;" +
                "varying vec2 vCoordinate;" +
                "void main() {" +
                "  gl_Position = aPosition;" +
                "  vCoordinate = aCoordinate;" +
                "}"
    }

    private fun getFragmentShader(): String {
        return "precision mediump float;" +
                "uniform sampler2D uTexture;" +
                "varying vec2 vCoordinate;" +
                "void main() {" +
                "  vec4 color = texture2D(uTexture, vCoordinate);" +
                "  gl_FragColor = color;" +
                "}"
    }

    private fun loadShader(
        type: Int, shaderCode:
        String
    ): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    override fun setTextureId(id: Int) {
        mTextureId = id
    }

    override fun release() {
        GLES20.glDisableVertexAttribArray(mVertexPosHandler)
        GLES20.glDisableVertexAttribArray(mTexturePosHandler)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glDeleteTextures(1, intArrayOf(mTextureId), 0)
        GLES20.glDeleteProgram(mProgress)
    }

    override fun setVideoSize(videoW: Int, videoH: Int) {

    }

    override fun setWorldSize(worldW: Int, worldH: Int) {

    }
}