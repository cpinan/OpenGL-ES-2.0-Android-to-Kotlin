package com.carlospinan.airhockeywithbettermallets

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.Matrix
import com.carlospinan.airhockeywithbettermallets.common.GLBaseRenderer
import com.carlospinan.airhockeywithbettermallets.extensions.loadTexture
import com.carlospinan.airhockeywithbettermallets.objects.Mallet
import com.carlospinan.airhockeywithbettermallets.objects.Puck
import com.carlospinan.airhockeywithbettermallets.objects.Table
import com.carlospinan.airhockeywithbettermallets.programs.ColorShaderProgram
import com.carlospinan.airhockeywithbettermallets.programs.TextureShaderProgram
import com.carlospinan.airhockeywithbettermallets.utilities.OpenGLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer(private val context: Context) : GLBaseRenderer() {

    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram

    private lateinit var puck: Puck
    private lateinit var table: Table
    private lateinit var mallet: Mallet

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private var texture = -1

    override fun onSurfaceCreated(unused: GL10?, eglConfig: EGLConfig?) {
        // Set the background clear color to red. The first component is
        // red, the second is green, the third is blue, and the last
        // component is alpha, which we don't use in this lesson.
        OpenGLES20.gl2ClearColor(
            red = 0.0f,
            green = 0.0f,
            blue = 0.0f,
            alpha = 0.0f
        )

        table = Table()
        mallet = Mallet(0.08f, 0.15f, 32)
        puck = Puck(0.06f, 0.02f, 32)

        textureProgram =
            TextureShaderProgram(
                context
            )
        colorProgram =
            ColorShaderProgram(
                context
            )

        texture = context.loadTexture(R.drawable.air_hockey_surface)

    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        OpenGLES20.gl2ViewPort(0, 0, width, height)

        Matrix.perspectiveM(
            projectionMatrix,
            0,
            45.0f,
            width.toFloat() / height.toFloat(),
            1.0f,
            10.0f
        )

        Matrix.setLookAtM(
            viewMatrix,
            0,
            0.0f,
            1.2f,
            2.2f,
            0f,
            0f,
            0f,
            0f,
            1f,
            0f
        )
    }

    override fun onDrawFrame(unused: GL10?) {
        // Clear the rendering surface.
        OpenGLES20.gl2Clear(GL_COLOR_BUFFER_BIT)

        Matrix.multiplyMM(
            viewProjectionMatrix,
            0,
            projectionMatrix,
            0,
            viewMatrix,
            0
        )

        // Draw the table.
        positionTableInScene()
        textureProgram.useProgram()
        textureProgram.setUniforms(modelViewProjectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        // Draw the mallets.
        positionObjectInScene(0f, mallet.height / 2f, -0.4f)
        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorProgram)
        mallet.draw()

        positionObjectInScene(0f, mallet.height / 2f, 0.4f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)

        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mallet.draw()

        // Draw the puck.
        positionObjectInScene(0f, puck.height / 2f, 0f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorProgram)
        puck.draw()

    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(
            modelMatrix,
            0,
            x,
            y,
            z
        )
        Matrix.multiplyMM(
            modelViewProjectionMatrix,
            0,
            viewProjectionMatrix,
            0,
            modelMatrix,
            0
        )
    }

    private fun positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(
            modelMatrix,
            0,
            -90f,
            1f,
            0f,
            0f
        )
        Matrix.multiplyMM(
            modelViewProjectionMatrix,
            0,
            viewProjectionMatrix,
            0,
            modelMatrix,
            0
        )
    }

}