package com.carlospinan.livewallpaper.wallpaper

import android.content.Context
import android.opengl.GLSurfaceView
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.carlospinan.livewallpaper.WallpaperRenderer
import com.carlospinan.livewallpaper.utilities.log

class GLWallpaperService : WallpaperService() {

    private val uiContext = GLWallpaperService@ this

    private inner class GLEngine : Engine() {

        private lateinit var glSurfaceView: WallpaperInnerSurfaceView
        private lateinit var renderer: WallpaperRenderer

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            log("onCreate($surfaceHolder)")
            glSurfaceView = WallpaperInnerSurfaceView(uiContext)
            renderer = WallpaperRenderer(uiContext)

            glSurfaceView.preserveEGLContextOnPause = true
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(renderer)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            log("onVisibilityChanged($visible)")
            if (visible) {
                glSurfaceView.onResume()
            } else {
                glSurfaceView.onPause()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            log("onDestroy()")
            glSurfaceView.onWallpaperDestroy()
        }

        override fun onOffsetsChanged(
            xOffset: Float,
            yOffset: Float,
            xOffsetStep: Float,
            yOffsetStep: Float,
            xPixelOffset: Int,
            yPixelOffset: Int
        ) {
            glSurfaceView.queueEvent {
                renderer.handleOffsetsChanged(xOffset, yOffset)
            }
        }

        inner class WallpaperInnerSurfaceView(context: Context) : GLSurfaceView(context) {

            override fun getHolder(): SurfaceHolder {
                log("Inner SV - $surfaceHolder")
                return surfaceHolder
            }

            fun onWallpaperDestroy() {
                log("onWallpaperDestroy")
                super.onDetachedFromWindow()
            }

        }

    }

    override fun onCreateEngine(): Engine {
        return GLEngine()
    }

}