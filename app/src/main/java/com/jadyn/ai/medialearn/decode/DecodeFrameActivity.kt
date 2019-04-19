package com.jadyn.ai.medialearn.decode

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.SeekBar
import com.jadyn.ai.medialearn.R
import com.jadyn.mediakit.function.durationSecond
import com.jadyn.mediakit.video.decode.VideoAnalyze
import com.jadyn.mediakit.video.decode.VideoDecoder2
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_decode_frame.*
import java.util.*

/**
 *@version:
 *@FileDescription:
 *@Author:jing
 *@Since:2019/2/19
 *@ChangeList:
 */
class DecodeFrameActivity : AppCompatActivity() {

    private val decodeMP4Path = TextUtils.concat(Environment.getExternalStorageDirectory().path,
            "/yazi.mp4").toString()

    private var videoAnalyze: VideoAnalyze? = null

    private var videoDecoder2: VideoDecoder2? = null

    private var dis: Disposable? = null

    //    private val e by lazy {
//        Executors.newSingleThreadExecutor()
//    }
//
//    inner class a(val cc: Int) : Runnable {
//        override fun run() {
//            Log.d("cece", "cc: $cc thread ${Thread.currentThread().name}")
//            try {
//                Thread.sleep(1000)
//                if (cc == 5) {
//                    return
//                }
//                Log.d("cece", "cc $cc succeed: ")
//                Log.d("cece", "pool shutdown ${e.isShutdown}: ")
//            } catch (e1: java.lang.Exception) {
//                Log.d("cece", "pool Exception shutdown ${e.isShutdown}: ")
//                Log.d("cece", "e: ${e1.message} ")
//            }
//        }
//
//    }
//
    private var count = 0
//
//    override fun onBackPressed() {
//        e.shutdownNow()
//        super.onBackPressed()
//    }
    
    private val q by lazy { 
        Stack<Int>()
    }

    private val thread by lazy {
        HandlerThread("decoder")
    }

    private val handler by lazy {
        thread.start()
        Handler(thread.looper)
    }

    private class ss(private val a: Int) : Runnable {
        override fun run() {
            Thread.sleep(3000)
            Log.d("ss", "num is $a: thread ${Thread.currentThread().name}")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decode_frame)

        file_frame_et.setText(decodeMP4Path)


        val a = arrayListOf(1, 2, 3, 4, 5, 6)

        a.forEach {
            if (it == 4) {
                return@forEach
            }
            Log.d("fuck", "it is $it: ")
        }

        sure_video.setOnClickListener {
            val dataSource = file_frame_et.text.toString()
            if (videoAnalyze != null && videoAnalyze!!.dataSource.equals(dataSource)) {
                return@setOnClickListener
            }
            videoAnalyze = VideoAnalyze(dataSource)
            video_seek.max = videoAnalyze!!.mediaFormat.durationSecond
            video_seek.progress = 0

            videoDecoder2 = VideoDecoder2(dataSource)
            updateTime(0, video_seek.max)
        }

        video_seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                videoAnalyze?.apply {
                    updateTime(progress, mediaFormat.durationSecond)
                }
                videoDecoder2?.apply {
                    getFrame(seekBar.progress.toFloat(), {
                        frame_img.setImageBitmap(it)
                    }, {
                        Log.d("cece", "throwable ${it.message}: ")
                    })
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                q.push(seekBar.progress)
                Log.d("cece", " q ${q.toString()} : ${q.peek()} ")
            }
        })

    }

    private fun updateTime(progress: Int, max: Int) {
        time.text = "现在 : $progress 总时长为 : $max"
    }

    override fun onDestroy() {
        super.onDestroy()
        videoDecoder2?.release()
        
        thread.quitSafely()
        handler.removeCallbacksAndMessages(null)
    }
}