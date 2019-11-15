package com.carlospinan.airhockeyortho

import android.content.Context
import android.opengl.GLES20.*
import com.carlospinan.airhockeyortho.common.GLBaseRenderer
import com.carlospinan.airhockeyortho.extensions.readTextFileFromResource
import com.carlospinan.airhockeyortho.utilities.OpenGLES20
import com.carlospinan.airhockeyortho.utilities.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val A_COLOR = "a_Color"
private const val A_POSITION = "a_Position"
private const val U_MATRIX = "u_Matrix"

private const val POSITION_COMPONENT_COUNT = 2
private const val COLOR_COMPONENT_COUNT = 3

private const val BYTES_PER_FLOAT = 4
private const val STRIDE =
    (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

class AirHockeyRenderer(private val context: Context) : GLBaseRenderer() {

    private var vertexData: FloatBuffer

    private var uMatrixLocation: Int = -1
    private var aColorLocation: Int = -1
    private var aPositionLocation: Int = -1

    private val projectionMatrix = FloatArray(16)

    //
    // Vertex data is stored in the following manner:
    //
    // The first two numbers are part of the position: X, Y
    // The next three numbers are part of the color: R, G, B
    //
    private val tableVerticesWithTriangles = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B

        // Triangle Fan
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 1f, 0f, 0f,

        // Mallets
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
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
         * the bytes can be ordered either from most significant to least signif- icant or from least to most.
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

        uMatrixLocation = OpenGLES20.gl2GetUniformLocation(programId, U_MATRIX)
        aPositionLocation = OpenGLES20.gl2GetAttribLocation(programId, A_POSITION)
        aColorLocation = OpenGLES20.gl2GetAttribLocation(programId, A_COLOR)

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexData.position(0)

        OpenGLES20.gl2VertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )

        OpenGLES20.gl2EnableVertexAttribArray(aPositionLocation)

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_COLOR_LOCATION.
        vertexData.position(POSITION_COMPONENT_COUNT)

        OpenGLES20.gl2VertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )

        OpenGLES20.gl2EnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        OpenGLES20.gl2ViewPort(0, 0, width, height)

        val aspectRatio = if (width > height)
            width.toFloat() / height.toFloat()
        else
            height.toFloat() / width.toFloat()

        if (width > height) {
            // Landscape
            OpenGLES20.orthographic(
                projectionMatrix,
                0,
                -aspectRatio,
                aspectRatio,
                -1.0f,
                1.0f,
                -1.0f,
                1.0f
            )
        } else {
            // Portrait or square
            OpenGLES20.orthographic(
                projectionMatrix,
                0,
                -1.0f,
                1.0f,
                -aspectRatio,
                aspectRatio,
                -1.0f,
                1.0f
            )
        }
    }

    override fun onDrawFrame(unused: GL10?) {
        // Clear the rendering surface.
        OpenGLES20.gl2Clear(GL_COLOR_BUFFER_BIT)

        // Assign the matrix
        OpenGLES20.gl2UniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix)

        // Draw the table
        OpenGLES20.gl2DrawArrays(GL_TRIANGLE_FAN, 0, 6)

        // Draw the center dividing line.
        OpenGLES20.gl2DrawArrays(GL_LINES, 6, 2)

        // Draw the first mallet blue.
        OpenGLES20.gl2DrawArrays(GL_POINTS, 8, 1)

        // Draw the second mallet red.
        OpenGLES20.gl2DrawArrays(GL_POINTS, 9, 1)

    }

}