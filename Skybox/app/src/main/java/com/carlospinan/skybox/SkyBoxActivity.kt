package com.carlospinan.skybox

import android.annotation.SuppressLint
import android.view.MotionEvent
import com.carlospinan.skybox.common.GLBaseActivity
import com.carlospinan.skybox.common.GLBaseRenderer

class SkyBoxActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return SkyBoxRenderer(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onReady() {
        var previousX = 0f
        var previousY = 0f

        glSurfaceView.setOnTouchListener { _, event ->
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

                    glSurfaceView.queueEvent {
                        renderer.handleTouchDrag(deltaX, deltaY)
                    }

                }
            }
            true
        }
    }
}
