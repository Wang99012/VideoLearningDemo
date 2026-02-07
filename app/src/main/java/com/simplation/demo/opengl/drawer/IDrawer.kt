package com.simplation.demo.opengl.drawer

import android.graphics.SurfaceTexture

interface IDrawer {
    fun draw()
    fun setTextureId(id: Int)
    fun release()

    //VideoDrawer 新增：用于提供SurfaceTexture
    fun getSurfaceTexture(
        cb: (st: SurfaceTexture) -> Unit
    ) {}

    //设置视频的宽高
    fun setVideoSize(videoW: Int, videoH: Int)
    //设置OpenGL的宽高
    fun setWorldSize(worldW: Int, worldH: Int)
}