package com.carlospinan.firstopenglproject

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FirstOpenGLProjectRenderer : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(unused: GL10?, eglConfig: EGLConfig?) {
        // Set the background clear color to red. The first component is
        // red, the second is green, the third is blue, and the last
        // component is alpha, which we don't use in this lesson.
        glClearColor(
            1.0f,
            0.0f,
            0.0f,
            0.0f
        )
    }

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
    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(
            0,
            0,
            width,
            height
        )
    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    override fun onDrawFrame(unused: GL10?) {
        // Clear the rendering surface.
        glClear(
            GL_COLOR_BUFFER_BIT
        )
    }

}