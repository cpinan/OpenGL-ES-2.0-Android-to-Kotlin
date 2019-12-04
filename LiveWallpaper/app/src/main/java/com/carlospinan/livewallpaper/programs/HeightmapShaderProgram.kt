package com.carlospinan.livewallpaper.programs

import android.content.Context
import android.opengl.GLES20.*
import com.carlospinan.livewallpaper.R


class HeightmapShaderProgram(
    context: Context
) : ShaderProgram(
    context,
    R.raw.heightmap_vertex_shader,
    R.raw.heightmap_fragment_shader
) {

    private val mvMatrixUniformLocation by lazy {
        glGetUniformLocation(program, U_MV_MATRIX)
    }

    private val itMVMatrixUniformLocation by lazy {
        glGetUniformLocation(program, U_IT_MV_MATRIX)
    }

    private val mvpMatrixUniformLocation by lazy {
        glGetUniformLocation(program, U_MVP_MATRIX)
    }

    private val pointLightPositionUniformLocation by lazy {
        glGetUniformLocation(program, U_POINT_LIGHT_POSITIONS)
    }

    private val pointLightColorsUniformLocation by lazy {
        glGetUniformLocation(program, U_POINT_LIGHT_COLORS)
    }

    private val vectorToLightUniformLocation by lazy {
        glGetUniformLocation(program, U_VECTOR_TO_LIGHT)
    }

    val positionAttributeLocation by lazy {
        glGetAttribLocation(program, A_POSITION)
    }

    val normalAttributeLocation by lazy {
        glGetAttribLocation(program, A_NORMAL)
    }

    fun setUniforms(
        mvMatrix: FloatArray,
        itMvMatrix: FloatArray,
        mvpMatrix: FloatArray,
        vectorToDirectionalLight: FloatArray,
        pointLightPositions: FloatArray,
        pointLightColors: FloatArray
    ) {
        glUniformMatrix4fv(mvMatrixUniformLocation, 1, false, mvMatrix, 0)
        glUniformMatrix4fv(itMVMatrixUniformLocation, 1, false, itMvMatrix, 0)
        glUniformMatrix4fv(mvpMatrixUniformLocation, 1, false, mvpMatrix, 0)

        glUniform3fv(vectorToLightUniformLocation, 1, vectorToDirectionalLight, 0)

        glUniform4fv(pointLightPositionUniformLocation, 3, pointLightPositions, 0)
        glUniform3fv(pointLightColorsUniformLocation, 3, pointLightColors, 0)
    }


}