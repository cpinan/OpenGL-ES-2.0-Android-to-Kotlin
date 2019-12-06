package com.carlospinan.particles.common

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class GLBaseActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView

    private lateinit var renderer: GLBaseRenderer

    abstract fun setRenderer(): GLBaseRenderer

    abstract fun onReady()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.renderer = setRenderer()
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(renderer)
        setContentView(glSurfaceView)
        onReady()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

}