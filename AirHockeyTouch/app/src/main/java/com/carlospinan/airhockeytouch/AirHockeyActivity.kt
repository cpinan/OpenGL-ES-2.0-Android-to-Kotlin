package com.carlospinan.airhockeytouch

import android.annotation.SuppressLint
import android.view.MotionEvent
import com.carlospinan.airhockeytouch.common.GLBaseActivity
import com.carlospinan.airhockeytouch.common.GLBaseRenderer

class AirHockeyActivity : GLBaseActivity() {

    override fun setRenderer(): GLBaseRenderer {
        return AirHockeyRenderer(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onReady() {

        glSurfaceView.setOnTouchListener { view, event ->
            // Convert touch coordinates into normalized device
            // coordinates, keeping in mind that Android's Y
            // coordinates are inverted.
            val normalizedX = (event.x / view.width.toFloat()) * 2 - 1
            val normalizedY = -((event.y / view.height.toFloat()) * 2 - 1)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    glSurfaceView.queueEvent {
                        renderer.handleTouchPress(
                            normalizedX,
                            normalizedY
                        )
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    glSurfaceView.queueEvent {
                        renderer.handleTouchDrag(
                            normalizedX,
                            normalizedY
                        )
                    }
                }
            }

            true
        }

    }

}