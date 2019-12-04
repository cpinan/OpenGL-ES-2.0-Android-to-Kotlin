package com.carlospinan.livewallpaper.programs

import android.content.Context
import com.carlospinan.livewallpaper.extensions.readTextFileFromResource
import com.carlospinan.livewallpaper.utilities.OpenGLES20
import com.carlospinan.livewallpaper.utilities.ShaderUtils

const val U_MATRIX = "u_Matrix"
const val U_TIME = "u_Time"
const val U_TEXTURE_UNIT = "u_TextureUnit"

const val A_POSITION = "a_Position"
const val A_COLOR = "a_Color"
const val A_DIRECTION_VECTOR = "a_DirectionVector"
const val A_PARTICLE_START_TIME = "a_ParticleStartTime"

const val U_VECTOR_TO_LIGHT = "u_VectorToLight"
const val A_NORMAL = "a_Normal"

const val U_MV_MATRIX = "u_MVMatrix"
const val U_IT_MV_MATRIX = "u_IT_MVMatrix"
const val U_MVP_MATRIX = "u_MVPMatrix"
const val U_POINT_LIGHT_POSITIONS = "u_PointLightPositions"
const val U_POINT_LIGHT_COLORS = "u_PointLightColors"

open class ShaderProgram(
    context: Context,
    vertexShaderResourceId: Int,
    fragmentShaderResourceId: Int
) {

    val program by lazy {
        ShaderUtils.buildProgram(
            context.readTextFileFromResource(vertexShaderResourceId),
            context.readTextFileFromResource(fragmentShaderResourceId)
        )
    }

    fun useProgram() {
        // Set the current OpenGL shader program to this program.
        OpenGLES20.gl2UseProgram(program)
    }

}