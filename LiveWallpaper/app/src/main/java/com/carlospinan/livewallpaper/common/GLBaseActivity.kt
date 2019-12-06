package com.carlospinan.livewallpaper.common

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class GLBaseActivity : AppCompatActivity() {

    lateinit var wallpaperSurfaceView: GLSurfaceView
        private set

    lateinit var renderer: GLBaseRenderer
        private set

    abstract fun setRenderer(): GLBaseRenderer

    abstract fun onReady()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.renderer = setRenderer()
        wallpaperSurfaceView = GLSurfaceView(this)
        wallpaperSurfaceView.setEGLContextClientVersion(2)
        wallpaperSurfaceView.setRenderer(renderer)
        setContentView(wallpaperSurfaceView)
        onReady()
    }

    override fun onPause() {
        super.onPause()
        wallpaperSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        wallpaperSurfaceView.onResume()
    }

}