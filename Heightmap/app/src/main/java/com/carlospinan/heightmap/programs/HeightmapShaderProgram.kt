package com.carlospinan.heightmap.programs

import android.content.Context
import android.opengl.GLES20.*
import com.carlospinan.heightmap.R


class HeightmapShaderProgram(
    context: Context
) : ShaderProgram(
    context,
    R.raw.heightmap_vertex_shader,
    R.raw.heightmap_fragment_shader
) {

    private val matrixUniformLocation by lazy {
        glGetUniformLocation(program, U_MATRIX)
    }
    val positionAttributeLocation by lazy {
        glGetAttribLocation(program, A_POSITION)
    }

    fun setUniforms(matrix: FloatArray) {
        glUniformMatrix4fv(
            matrixUniformLocation,
            1,
            false,
            matrix,
            0
        )
    }


}