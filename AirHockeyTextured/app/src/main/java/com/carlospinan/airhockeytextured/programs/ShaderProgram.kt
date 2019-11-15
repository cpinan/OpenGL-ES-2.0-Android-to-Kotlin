package com.carlospinan.airhockeytextured.programs

import android.content.Context
import com.carlospinan.airhockeytextured.extensions.readTextFileFromResource
import com.carlospinan.airhockeytextured.utilities.OpenGLES20
import com.carlospinan.airhockeytextured.utilities.ShaderUtils

const val U_MATRIX = "u_Matrix"
const val U_TEXTURE_UNIT = "u_TextureUnit"
const val A_POSITION = "a_Position"
const val A_COLOR = "a_Color"
const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

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