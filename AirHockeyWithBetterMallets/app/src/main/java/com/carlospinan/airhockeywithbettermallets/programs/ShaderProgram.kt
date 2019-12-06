package com.carlospinan.airhockeywithbettermallets.programs

import android.content.Context
import com.carlospinan.airhockeywithbettermallets.extensions.readTextFileFromResource
import com.carlospinan.airhockeywithbettermallets.utilities.OpenGLES20
import com.carlospinan.airhockeywithbettermallets.utilities.ShaderUtils

const val U_MATRIX = "u_Matrix"
const val U_TEXTURE_UNIT = "u_TextureUnit"
const val A_POSITION = "a_Position"
const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
const val U_COLOR = "u_Color"

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