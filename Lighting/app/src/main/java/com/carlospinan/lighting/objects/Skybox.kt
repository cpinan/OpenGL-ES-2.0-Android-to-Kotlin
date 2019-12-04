package com.carlospinan.lighting.objects

import android.opengl.GLES20.*
import com.carlospinan.lighting.data.VertexArray
import com.carlospinan.lighting.programs.SkyboxShaderProgram
import java.nio.ByteBuffer

private const val POSITION_COMPONENT_COUNT = 3

class Skybox {

    private val vertexArray = VertexArray(
        floatArrayOf(
            -1f, 1f, 1f,        // (0) Top-left near
            1f, 1f, 1f,         // (1) Top-right near
            -1f, -1f, 1f,       // (2) Bottom-left near
            1f, -1f, 1f,        // (3) Bottom-right near
            -1f, 1f, -1f,       // (4) Top-left far
            1f, 1f, -1f,        // (5) Top-right far
            -1f, -1f, -1f,      // (6) Bottom-left far
            1f, -1f, -1f        // (7) Bottom-right far
        )
    )
    private val indexArray = ByteBuffer.allocate(6 * 6)
        .put(
            byteArrayOf(
                // Front
                1, 3, 0,
                0, 3, 2,

                // Back
                4, 6, 5,
                5, 6, 7,

                // Left
                0, 2, 4,
                4, 2, 6,

                // Right
                5, 7, 1,
                1, 7, 3,

                // Top
                5, 1, 4,
                4, 1, 0,

                // Bottom
                6, 2, 7,
                7, 2, 3
            )
        ).apply {
            position(0)
        }

    fun bindData(program: SkyboxShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            program.positionAttributeLocation,
            POSITION_COMPONENT_COUNT,
            0
        )
    }

    fun draw() {
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray)
    }


}