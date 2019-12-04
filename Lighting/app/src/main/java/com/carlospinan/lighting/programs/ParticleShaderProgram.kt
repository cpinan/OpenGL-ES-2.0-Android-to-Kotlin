package com.carlospinan.lighting.programs

import android.content.Context
import android.opengl.GLES20
import com.carlospinan.lighting.R
import com.carlospinan.lighting.utilities.OpenGLES20

class ParticleShaderProgram(
    context: Context
) : ShaderProgram(
    context,
    R.raw.particle_vertex_shader,
    R.raw.particle_fragment_shader
) {

    // Uniform locations
    //      Retrieve uniform locations for the shader program.
    val matrixUniformLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(program, U_MATRIX)
    }

    val timeUniformLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(program, U_TIME)
    }

    val textureUniformLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(program, U_TEXTURE_UNIT)
    }

    // Attribute locations
    //      Retrieve attribute locations for the shader program.
    val positionAttributeLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(program, A_POSITION)
    }

    val colorAttributeLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(program, A_COLOR)
    }

    val directionVectorAttributeLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(program, A_DIRECTION_VECTOR)
    }

    val particleStartTimeAttributeLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(program, A_PARTICLE_START_TIME)
    }

    fun setUniforms(matrix: FloatArray, elapsedTime: Float, textureId: Int) {
        OpenGLES20.gl2UniformMatrix4fv(
            matrixUniformLocation,
            1,
            false,
            matrix,
            0
        )
        GLES20.glUniform1f(
            timeUniformLocation,
            elapsedTime
        )
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(
            textureUniformLocation,
            0
        )
    }

}