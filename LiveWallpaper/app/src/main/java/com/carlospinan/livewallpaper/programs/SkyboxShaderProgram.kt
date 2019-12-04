package com.carlospinan.livewallpaper.programs

import android.content.Context
import android.opengl.GLES20.*
import com.carlospinan.livewallpaper.R
import com.carlospinan.livewallpaper.utilities.OpenGLES20

class SkyboxShaderProgram(
    context: Context
) : ShaderProgram(
    context,
    R.raw.skybox_vertex_shader,
    R.raw.skybox_fragment_shader
) {

    // Uniform locations
    //      Retrieve uniform locations for the shader program.
    val matrixUniformLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(program, U_MATRIX)
    }

    val textureUniformLocation by lazy {
        OpenGLES20.gl2GetUniformLocation(program, U_TEXTURE_UNIT)
    }

    // Attribute locations
    //      Retrieve attribute locations for the shader program.
    val positionAttributeLocation by lazy {
        OpenGLES20.gl2GetAttribLocation(program, A_POSITION)
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        glUniformMatrix4fv(
            matrixUniformLocation,
            1,
            false,
            matrix,
            0
        )

        glActiveTexture(GL_TEXTURE0)

        glBindTexture(GL_TEXTURE_CUBE_MAP, textureId)

        glUniform1i(textureUniformLocation, 0)
    }

}