package com.carlospinan.airhockey1.common

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class GLBaseRenderer : GLSurfaceView.Renderer {

    abstract override fun onSurfaceCreated(unused: GL10?, eglConfig: EGLConfig?)

    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one created.
     *
     * @param width
     *            The new width, in pixels.
     * @param height
     *            The new height, in pixels.
     */
    abstract override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int)

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    abstract override fun onDrawFrame(unused: GL10?)

}