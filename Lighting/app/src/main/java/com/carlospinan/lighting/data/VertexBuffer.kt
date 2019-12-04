package com.carlospinan.lighting.data

import android.opengl.GLES20.*
import com.carlospinan.lighting.common.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class VertexBuffer(
    private val vertexData: FloatArray
) {

    val bufferId: Int

    init {
        val buffers = IntArray(1)
        glGenBuffers(buffers.size, buffers, 0)

        if (buffers[0] == 0) {
            throw RuntimeException("Could not create a new index buffer object.")
        }

        bufferId = buffers[0]

        // Bind to the buffer.
        glBindBuffer(GL_ARRAY_BUFFER, buffers[0])

        // Transfer data to native memory.
        val vertexArray: FloatBuffer = ByteBuffer
            .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
        vertexArray.position(0)

        // Transfer data from native memory to the GPU buffer.
        glBufferData(
            GL_ARRAY_BUFFER,
            vertexArray.capacity() * BYTES_PER_FLOAT,
            vertexArray,
            GL_STATIC_DRAW
        )

        // IMPORTANT: Unbind from the buffer when we're done with it.
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        // We let vertexArray go out of scope, but it won't be released
        // until the next time the garbage collector is run.
    }

    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        glBindBuffer(GL_ARRAY_BUFFER, bufferId)
        // This call is slightly different than the glVertexAttribPointer we've
        // used in the past: the last parameter is set to dataOffset, to tell OpenGL
        // to begin reading data at this position of the currently bound buffer.
        glVertexAttribPointer(
            attributeLocation,
            componentCount,
            GL_FLOAT,
            false,
            stride,
            dataOffset
        )
        glEnableVertexAttribArray(attributeLocation)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

}