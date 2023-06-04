package ru.startandroid.develop.camerafeatures

import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity() {

    private var surfaceView: SurfaceView? = null
    var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceView = findViewById<View>(R.id.surfaceView) as SurfaceView

        val holder: SurfaceHolder = surfaceView!!.holder
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    camera!!.setPreviewDisplay(holder)
                    camera!!.startPreview()
                } catch (e:java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder,
                                        format: Int,
                                        width: Int,
                                        height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        camera = Camera.open()
        initSpinners()
    }

    override fun onPause() {
        super.onPause()
        if (camera != null) {
            camera!!.release()
        }
        camera = null
    }

    private fun initSpinners() {
        val colorEffect = camera!!.parameters.supportedColorEffects
        val spEffect = initSpinner(R.id.spEffect, colorEffect, camera!!.parameters.colorEffect)
        spEffect.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, arg2: Int, arg3: Long) {
                val params = camera!!.parameters
                params.colorEffect = colorEffect[arg2]
                camera!!.parameters = params
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
            }
        }
    }

    private fun initSpinner(spinnerId: Int, data: List<String>, currentValue: String): Spinner {
        val spinner: Spinner = findViewById<View>(spinnerId) as Spinner
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        for (i in data.indices) {
            val item = data[i]
            if (item == currentValue) {
                spinner.setSelection(i)
            }
        }
        return spinner
     }
}
