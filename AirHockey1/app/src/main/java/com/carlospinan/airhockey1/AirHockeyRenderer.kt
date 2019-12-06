package com.carlospinan.airhockey1

import android.content.Context
import android.opengl.GLES20.*
import com.carlospinan.airhockey1.common.GLBaseRenderer
import com.carlospinan.airhockey1.extensions.readTextFileFromResource
import com.carlospinan.airhockey1.utilities.OpenGLES20
import com.carlospinan.airhockey1.utilities.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val U_COLOR = "u_Color"
private const val A_POSITION = "a_Position"

private const val POSITION_COMPONENT_COUNT = 2
private const val BYTES_PER_FLOAT = 4

class AirHockeyRenderer(private val context: Context) : GLBaseRenderer() {

    private var vertexData: FloatBuffer

    private var uColorLocation: Int = -1
    private var aPositionLocation: Int = -1

    private val tableVerticesWithTriangles = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,

        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,

        // Line 1
        -0.5f, 0f,
        0.5f, 0f,

        // Mallets
        0f, -0.25f,
        0f, 0.25f
    )

    init {
        /**
         * First we allocated a block of native memory using ByteBuffer.allocateDirect();
         * this memory will not be managed by the garbage collector.
         *
         * We need to tell the method how large the block of memory should be in bytes.
         * Since our vertices are stored in an array of floats and there are 4 bytes per float,
         * we pass in tableVerticesWithTriangles.length * BYTES_PER_FLOAT.
         *
         * The next line tells the byte buffer that it should organize its bytes in native order.
         * When it comes to values that span multiple bytes, such as 32-bit integers,
         * the bytes can be ordered either from most significant to least sign-if- i-cant or from least to most.
         * Think of this as similar to writing a number either from left to right or right to left.
         * It’s not important for us to know what that order is,
         * but it is important that we use the same order as the platform.
         * We do this by calling order(ByteOrder.nativeOrder()).
         *
         */
        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(tableVerticesWithTriangles)
    }

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

        val vertexShaderSource = context.readTextFileFromResource(R.raw.simple_vertex_shader)
        val fragmentShaderSource = context.readTextFileFromResource(R.raw.simple_fragment_shader)

        val vertexShaderId = ShaderUtils.compileVertexShader(vertexShaderSource)
        val fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShaderSource)

        val programId = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)

        ShaderUtils.validateProgram(programId)

        OpenGLES20.gl2UseProgram(programId)

        uColorLocation = OpenGLES20.gl2GetUniformLocation(programId, U_COLOR)
        aPositionLocation = OpenGLES20.gl2GetAttribLocation(programId, A_POSITION)

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexData.position(0)

        OpenGLES20.gl2VertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            0,
            vertexData
        )

        OpenGLES20.gl2EnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        OpenGLES20.gl2ViewPort(0, 0, width, height)
    }

    override fun onDrawFrame(unused: GL10?) {
        // Clear the rendering surface.
        OpenGLES20.gl2Clear(GL_COLOR_BUFFER_BIT)

        // Draw the table
        OpenGLES20.gl2Uniform4f(
            uColorLocation,
            1.0f, 1.0f, 1.0f, 1.0f
        )
        OpenGLES20.gl2DrawArrays(GL_TRIANGLES, 0, 6)

        // Draw the center dividing line.
        OpenGLES20.gl2Uniform4f(
            uColorLocation,
            1.0f, 0.0f, 0.0f, 1.0f
        )
        OpenGLES20.gl2DrawArrays(GL_LINES, 6, 2)

        // Draw the first mallet blue.
        OpenGLES20.gl2Uniform4f(
            uColorLocation,
            0.0f, 0.0f, 1.0f, 1.0f
        )
        OpenGLES20.gl2DrawArrays(GL_POINTS, 8, 1)

        // Draw the second mallet red.
        OpenGLES20.gl2Uniform4f(
            uColorLocation,
            1.0f, 0.0f, 0.0f, 1.0f
        )
        OpenGLES20.gl2DrawArrays(GL_POINTS, 9, 1)

    }

}