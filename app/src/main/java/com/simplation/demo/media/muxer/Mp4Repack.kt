package com.simplation.demo.media.muxer

import android.media.MediaCodec
import android.util.Log
import com.simplation.demo.media.extractor.AudioExtractor
import com.simplation.demo.media.extractor.VideoExtractor
import java.nio.ByteBuffer

class Mp4Repack(path: String) {
    private val TAG = "Mp4Repack"

    //初始化音视频分离器
    private var mVExtractor = VideoExtractor(path)
    private var mAExtractor = AudioExtractor(path)

    //初始化封装器
    private val mMuxer: MMuxer = MMuxer()

    /**
     * 启动封装器
     */
    fun start() {
        val audioFormat = mAExtractor.getFormat()
        val videoFormat = mVExtractor.getFormat()

        //判断是否有音频数据，没有音频数据则告诉封装器，忽略音频频轨道
        if (audioFormat != null) {
            mMuxer.addAudioTrack(audioFormat)
        } else {
            mMuxer.setNoAudio()
        }

        //判断是否有视频数据，没有视频数据则告诉封装器，忽略视频轨道
        if (videoFormat != null) {
            mMuxer.addVideoTrack(videoFormat)
        } else {
            mMuxer.setNoVideo()
        }

        //启动线程
        Thread {
            val buffer = ByteBuffer.allocate(500 * 1024)
            val bufferInfo = MediaCodec.BufferInfo()

            //音频数据分离和写入
            if (audioFormat != null) {
                var size = mAExtractor.readBuffer(buffer)

                while (size > 0) {
                    bufferInfo.set(0, size, mAExtractor.getCurrentTimestamp(), mAExtractor.getSampleFlag())
                    mMuxer.writeAudioData(buffer, bufferInfo)

                    size = mAExtractor.readBuffer(buffer)
                }
            }

            //视频数据分离和写入
            if (videoFormat != null) {
                var size = mVExtractor.readBuffer(buffer)

                while (size > 0) {
                    bufferInfo.set(0, size, mVExtractor.getCurrentTimestamp(), mVExtractor.getSampleFlag())
                    mMuxer.writeVideoData(buffer, bufferInfo)

                    size = mVExtractor.readBuffer(buffer)
                }
            }

            mVExtractor.stop()
            mAExtractor.stop()
            mMuxer.release()
            Log.i(TAG, "MP4 重打包完成")
        }.start()
    }
}