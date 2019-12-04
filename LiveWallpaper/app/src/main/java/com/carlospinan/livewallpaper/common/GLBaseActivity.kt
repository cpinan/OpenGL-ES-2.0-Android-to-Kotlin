package com.carlospinan.livewallpaper.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carlospinan.livewallpaper.wallpaper.WallpaperGLSurfaceView

abstract class GLBaseActivity : AppCompatActivity() {

    lateinit var wallpaperSurfaceView: WallpaperGLSurfaceView
        private set

    lateinit var renderer: GLBaseRenderer
        private set

    abstract fun setRenderer(): GLBaseRenderer

    abstract fun onReady()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.renderer = setRenderer()
        wallpaperSurfaceView = WallpaperGLSurfaceView(this)
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