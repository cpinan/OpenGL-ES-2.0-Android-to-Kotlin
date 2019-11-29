package com.carlospinan.airhockeywithbettermallets.objects

import android.opengl.GLES20.GL_TRIANGLE_FAN
import com.carlospinan.airhockeywithbettermallets.common.BYTES_PER_FLOAT
import com.carlospinan.airhockeywithbettermallets.data.VertexArray
import com.carlospinan.airhockeywithbettermallets.programs.TextureShaderProgram
import com.carlospinan.airhockeywithbettermallets.utilities.OpenGLES20

private const val POSITION_COMPONENT_COUNT = 2
private const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
private const val STRIDE =
    (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT

class Table {

    private val vertexData = floatArrayOf(
        // Order of coordinates: X, Y, S, T

        // Triangle Fan
        0f, 0f, 0.5f, 0.5f,
        -0.5f, -0.8f, 0f, 0.9f,
        0.5f, -0.8f, 1f, 0.9f,
        0.5f, 0.8f, 1f, 0.1f,
        -0.5f, 0.8f, 0f, 0.1f,
        -0.5f, -0.8f, 0f, 0.9f
    )

    private val vertexArray =
        VertexArray(vertexData)

    fun draw() {
        OpenGLES20.gl2DrawArrays(GL_TRIANGLE_FAN, 0, 6)
    }

    fun bindData(program: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            program.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )

        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            program.aTextureCoordinatesLocation,
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        )
    }

}