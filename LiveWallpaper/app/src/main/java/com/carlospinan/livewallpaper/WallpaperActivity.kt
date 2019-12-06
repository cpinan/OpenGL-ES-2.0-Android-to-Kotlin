package com.carlospinan.livewallpaper

import android.annotation.SuppressLint
import android.view.MotionEvent
import com.carlospinan.livewallpaper.common.GLBaseActivity
import com.carlospinan.livewallpaper.common.GLBaseRenderer

class WallpaperActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return WallpaperRenderer(applicationContext)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onReady() {
        var previousX = 0f
        var previousY = 0f

        wallpaperSurfaceView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    previousX = event.x
                    previousY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - previousX
                    val deltaY = event.y - previousY

                    previousX = event.x
                    previousY = event.y

                    wallpaperSurfaceView.queueEvent {
                        renderer.handleTouchDrag(deltaX, deltaY)
                    }

                }
            }
            true
        }
    }
}
