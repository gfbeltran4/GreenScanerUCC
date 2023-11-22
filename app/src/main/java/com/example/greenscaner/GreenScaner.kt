package com.example.greenscaner

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.greenscaner.databinding.ActivityGreenScanerBinding
import com.example.greenscaner.sampledata.ClassifyImageTf
import com.example.greenscaner.sampledata.ReturnInterpreter
import com.ingenieriiajhr.jhrCameraX.BitmapResponse
import com.ingenieriiajhr.jhrCameraX.CameraJhr

class GreenScaner : AppCompatActivity() {
    lateinit var binding: ActivityGreenScanerBinding
    lateinit var cameraJhr: CameraJhr
    lateinit var classifyImageTf: ClassifyImageTf
    companion object {
        const val INPUT_SIZE = 224
        const val OUTPUT_SIZE = 3 // Número de etiquetas de salida
    }
    val classes = arrayOf("Rosa Saludable", "Enfermedad Oídio", "Plaga Mosca de sierra")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGreenScanerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraJhr = CameraJhr(this)

        classifyImageTf = ClassifyImageTf(this)
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (cameraJhr.allpermissionsGranted() && !cameraJhr.ifStartCamera) {
            startCameraJhr()
        } else {
            cameraJhr.noPermissions()
        }
    }
    private fun startCameraJhr() {
        var timeRepeat = System.currentTimeMillis()
        cameraJhr.addlistenerBitmap(object : BitmapResponse {
            override fun bitmapReturn(bitmap: Bitmap?) {
                if (bitmap != null) {
                    if (System.currentTimeMillis() > timeRepeat + 1000) {
                        classifyImage(bitmap)
                        timeRepeat = System.currentTimeMillis()
                    }

                }
            }
        })
        cameraJhr.initBitmap()
        cameraJhr.initImageProxy()
        cameraJhr.start(1, 0, binding.cameraPreview, true, false, true)
    }
    private fun classifyImage(img: Bitmap?) {
        val imgReScale = Bitmap.createScaledBitmap(img!!, INPUT_SIZE, INPUT_SIZE, false)

        classifyImageTf.listenerInterpreter(object : ReturnInterpreter {
            override fun classify(confidence: FloatArray, maxConfidence: Int) {
                binding.txtResult.UiThread(" ${confidence[0].decimal()} \n  ${confidence[1].decimal()} \n  ${confidence[2].decimal()} \n Diagnóstico: ${classes[maxConfidence]}")
            }
        })
        runOnUiThread {
            binding.imgPreview.setImageBitmap(imgReScale)
        }
        classifyImageTf.classify(imgReScale)
    }
    private fun TextView.UiThread(string: String) {
        runOnUiThread {
            this.text = string
        }
    }
    private fun Float.decimal(): String {
        return "%.2f".format(this)
    }
}


