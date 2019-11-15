package com.carlospinan.airhockeytextured

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.Matrix
import com.carlospinan.airhockeytextured.common.BYTES_PER_FLOAT
import com.carlospinan.airhockeytextured.common.GLBaseRenderer
import com.carlospinan.airhockeytextured.extensions.loadTexture
import com.carlospinan.airhockeytextured.objects.Mallet
import com.carlospinan.airhockeytextured.objects.Table
import com.carlospinan.airhockeytextured.programs.ColorShaderProgram
import com.carlospinan.airhockeytextured.programs.TextureShaderProgram
import com.carlospinan.airhockeytextured.utilities.OpenGLES20
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val A_COLOR = "a_Color"
private const val A_POSITION = "a_Position"
private const val U_MATRIX = "u_Matrix"

private const val POSITION_COMPONENT_COUNT = 2
private const val COLOR_COMPONENT_COUNT = 3

private const val STRIDE =
    (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

class AirHockeyRenderer(private val context: Context) : GLBaseRenderer() {

    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram
    private lateinit var table: Table
    private lateinit var mallet: Mallet

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

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
        mallet = Mallet()

        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)

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

        Matrix.setIdentityM(modelMatrix, 0)

        Matrix.translateM(
            modelMatrix,
            0,
            0.0f,
            0.0f,
            -2.5f
        )

        Matrix.rotateM(
            modelMatrix,
            0,
            -60.0f,
            1.0f,
            0.0f,
            0.0f
        )

        val temp = FloatArray(16)

        Matrix.multiplyMM(
            temp,
            0,
            projectionMatrix,
            0,
            modelMatrix,
            0
        )

        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)
    }

    override fun onDrawFrame(unused: GL10?) {
        // Clear the rendering surface.
        OpenGLES20.gl2Clear(GL_COLOR_BUFFER_BIT)

        // Draw the table.
        textureProgram.useProgram()
        textureProgram.setUniforms(projectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        // Draw the mallets.
        colorProgram.useProgram()
        colorProgram.setUniforms(projectionMatrix)
        mallet.bindData(colorProgram)
        mallet.draw()
    }

}