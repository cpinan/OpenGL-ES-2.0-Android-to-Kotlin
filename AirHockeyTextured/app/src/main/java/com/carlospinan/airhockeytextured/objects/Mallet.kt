package com.carlospinan.airhockeytextured.objects

import android.opengl.GLES20
import com.carlospinan.airhockeytextured.common.BYTES_PER_FLOAT
import com.carlospinan.airhockeytextured.data.VertexArray
import com.carlospinan.airhockeytextured.programs.ColorShaderProgram
import com.carlospinan.airhockeytextured.utilities.OpenGLES20


private const val POSITION_COMPONENT_COUNT = 2
private const val COLOR_COMPONENT_COUNT = 3
private const val STRIDE =
    (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

class Mallet {

    private val vertexData = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
    )

    private val vertexArray = VertexArray(vertexData)

    fun draw() {
        OpenGLES20.gl2DrawArrays(GLES20.GL_POINTS, 0, 2)
    }

    fun bindData(program: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            program.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )

        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            program.aColorLocation,
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
    }

}