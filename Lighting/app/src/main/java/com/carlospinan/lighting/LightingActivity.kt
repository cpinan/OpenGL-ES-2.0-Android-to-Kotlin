package com.carlospinan.lighting

import android.view.MotionEvent
import com.carlospinan.lighting.common.GLBaseActivity
import com.carlospinan.lighting.common.GLBaseRenderer

class LightingActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return LightingRenderer(this)
    }

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
