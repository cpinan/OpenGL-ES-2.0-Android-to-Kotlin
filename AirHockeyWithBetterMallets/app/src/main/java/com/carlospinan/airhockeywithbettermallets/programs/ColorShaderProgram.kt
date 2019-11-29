package com.carlospinan.airhockeywithbettermallets.programs

import android.content.Context
import com.carlospinan.airhockeywithbettermallets.R
import com.carlospinan.airhockeywithbettermallets.utilities.OpenGLES20

class ColorShaderProgram(
    context: Context
) : ShaderProgram(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {

    val uMatrixLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(program,
            U_MATRIX
        )
    }

    val aPositionLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(program,
            A_POSITION
        )
    }

    val aColorLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(program,
            A_COLOR
        )
    }

    fun setUniforms(matrix: FloatArray) {
        // Pass the matrix into the shader program.
        OpenGLES20.gl2UniformMatrix4fv(
            uMatrixLocation,
            1,
            false,
            matrix,
            0
        )
    }

}