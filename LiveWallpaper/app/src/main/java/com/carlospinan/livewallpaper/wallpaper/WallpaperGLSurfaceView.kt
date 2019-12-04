package com.carlospinan.livewallpaper.wallpaper

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.SurfaceHolder
import com.carlospinan.livewallpaper.utilities.log

class WallpaperGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private lateinit var surfaceHolder: SurfaceHolder

    override fun getHolder(): SurfaceHolder {
        log("getHolder()")
        if (::surfaceHolder.isInitialized) {
            log("Holder Initialized!!! --> $surfaceHolder")
            return surfaceHolder
        }
        return super.getHolder()
    }

    fun onWallpaperDestroy() {
        log("onWallpaperDestroy")
        super.onDetachedFromWindow()
    }

    fun setSurfaceHolder(surfaceHolder: SurfaceHolder) {
        log("setSurfaceHolder --> $surfaceHolder")
        this.surfaceHolder = surfaceHolder
    }

}