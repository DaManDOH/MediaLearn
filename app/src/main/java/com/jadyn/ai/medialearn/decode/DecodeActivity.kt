package com.jadyn.ai.medialearn.decode

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.jadyn.ai.medialearn.R
import com.jadyn.mediakit.video.decode.VideoDecoder
import kotlinx.android.synthetic.main.activity_decode.*
import java.io.File

/**
 *@version:
 *@FileDescription:
 *@Author:jing
 *@Since:2019/1/22
 *@ChangeList:
 */
class DecodeActivity : AppCompatActivity() {

    private val decodeMP4Path = TextUtils.concat(Environment.getExternalStorageDirectory().path,
            "/yazi.mp4").toString()

    private var videoDecoder: VideoDecoder? = null

    private var filePath = ""

    private var outputPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decode)

        // 2019/2/14-16:25 set a default test video address
        file_et.setText(decodeMP4Path)
        resetOutputEt()

        sure_file_tv.setOnClickListener {
            checkFile()
        }

        sure_output_tv.setOnClickListener {
            checkOutputPath()
        }


        switch_iv.setOnClickListener {
            if (videoDecoder != null) {
                Toast.makeText(this, "Decoding", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            switch_iv.isSelected = !switch_iv.isSelected
            switch_iv.setImageResource(if (switch_iv.isSelected) R.drawable.p else R.drawable.c)
        }

        start_tv.setOnClickListener {
            checkFile()
            checkOutputPath()
            if (videoDecoder != null) {
                Toast.makeText(this, "Decoding", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            videoDecoder = VideoDecoder(filePath, outputPath)
            videoDecoder!!.start({
                videoDecoder = null
            }, {
                Log.d("cece", "decode failed : $it")
                videoDecoder = null
            }, {
                this@DecodeActivity.runOnUiThread {
                    val s = if (switch_iv.isSelected) "OpenGL render" else "YUV memory"
                    output_loading_tv.text = TextUtils.concat(s, " decoding, frame $it")
                }
            })
        }
    }

    private fun checkOutputPath() {
        val f = output_et.text.toString()
        if (f.isBlank()) {
            Toast.makeText(this, "Cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        val file = File(f)
        if (!file.exists()) {
            file.mkdirs()
        }
        if (!file.isDirectory) {
            Toast.makeText(this, "Must be a folder", Toast.LENGTH_SHORT).show()
            resetOutputEt()
            return
        }
        outputPath = f
    }

    private fun checkFile() {
        val f = file_et.text.toString()
        if (f.isBlank()) {
            Toast.makeText(this, "Cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        val file = File(f)
        if (!file.exists() || !file.isFile) {
            Toast.makeText(this, "File error", Toast.LENGTH_SHORT).show()
            file_et.setText("")
            return
        }
        filePath = f
    }

    private fun resetOutputEt() {
        output_et.setText(TextUtils.concat(Environment.getExternalStorageDirectory().path, "/"))
        output_et.setSelection(output_et.text.toString().length)
    }

    override fun onDestroy() {
        super.onDestroy()
        videoDecoder?.release()
    }
}