package com.carlospinan.airhockeytouch.programs

import android.content.Context
import android.opengl.GLES20.GL_TEXTURE0
import android.opengl.GLES20.GL_TEXTURE_2D
import com.carlospinan.airhockeytouch.R
import com.carlospinan.airhockeytouch.utilities.OpenGLES20

class TextureShaderProgram(
    context: Context
) : ShaderProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {

    private val uMatrixLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(
            program,
            U_MATRIX
        )
    }

    private val uTextureUnitLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(
            program,
            U_TEXTURE_UNIT
        )
    }

    val aPositionLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(
            program,
            A_POSITION
        )
    }

    val aTextureCoordinatesLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(
            program,
            A_TEXTURE_COORDINATES
        )
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        // Pass the matrix into the shader program.
        OpenGLES20.gl2UniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        // Set the active texture unit to texture unit 0.
        OpenGLES20.gl2ActiveTexture(GL_TEXTURE0)

        // Bind the texture to this unit.
        OpenGLES20.gl2BindTexture(GL_TEXTURE_2D, textureId)

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        OpenGLES20.gl2Uniform1i(uTextureUnitLocation, 0)
    }

}