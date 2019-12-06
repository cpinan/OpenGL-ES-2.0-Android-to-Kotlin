package com.carlospinan.airhockeytouch.programs

import android.content.Context
import com.carlospinan.airhockeytouch.R
import com.carlospinan.airhockeytouch.utilities.OpenGLES20

class ColorShaderProgram(
    context: Context
) : ShaderProgram(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {

    private val uMatrixLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(
            program,
            U_MATRIX
        )
    }

    val aPositionLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(
            program,
            A_POSITION
        )
    }

    private val uColorLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(
            program,
            U_COLOR
        )
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        // Pass the matrix into the shader program.
        OpenGLES20.gl2UniformMatrix4fv(
            uMatrixLocation,
            1,
            false,
            matrix,
            0
        )

        OpenGLES20.gl2Uniform4f(
            uColorLocation,
            r,
            g,
            b,
            1f
        )
    }

}