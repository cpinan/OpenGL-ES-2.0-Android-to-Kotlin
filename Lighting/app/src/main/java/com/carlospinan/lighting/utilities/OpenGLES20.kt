package com.carlospinan.lighting.utilities

import android.opengl.GLES20.*

object OpenGLES20 {

    // CREATIONAL

    fun gl2GenTextures(n: Int, texture: IntArray, offset: Int = 0) {
        glGenTextures(n, texture, offset)
    }

    fun gl2CreateProgram(): Int = glCreateProgram()

    fun gl2CreateShader(type: Int): Int {
        return glCreateShader(type)
    }

    fun gl2ShaderSource(shaderId: Int, shaderCode: String) {
        glShaderSource(shaderId, shaderCode)
    }

    fun gl2CompileShader(shaderId: Int) {
        glCompileShader(shaderId)
    }

    fun gl2GetShaderIv(shaderId: Int, params: IntArray, offset: Int = 0) {
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, params, offset)
    }

    fun gl2DeleteShader(shaderId: Int) {
        glDeleteShader(shaderId)
    }

    fun gl2GetShaderInfoLog(shaderId: Int) {
        glGetShaderInfoLog(shaderId)
    }

    fun gl2AttachShader(programObjectId: Int, shaderId: Int) {
        glAttachShader(programObjectId, shaderId)
    }

    fun gl2LinkProgram(programObjectId: Int) {
        glLinkProgram(programObjectId)
    }

    fun gl2GetProgramiv(programObjectId: Int, status: Int, params: IntArray, offset: Int = 0) {
        glGetProgramiv(programObjectId, status, params, offset)
    }

    fun gl2DeleteProgram(programObjectId: Int) {
        glDeleteProgram(programObjectId)
    }

    fun gl2ValidateProgram(programObjectId: Int) {
        glValidateProgram(programObjectId)
    }

    fun gl2UseProgram(program: Int) {
        glUseProgram(program)
    }

    // RENDERER

    fun gl2GetUniformLocation(program: Int, name: String): Int {
        return glGetUniformLocation(program, name)
    }

    fun gl2GetAttribLocation(program: Int, name: String): Int {
        return glGetAttribLocation(program, name)
    }

    fun gl2UniformMatrix4fv(
        location: Int = 0,
        count: Int = 0,
        transpose: Boolean = false,
        value: FloatArray = floatArrayOf(),
        offset: Int = 0
    ) {
        glUniformMatrix4fv(
            location,
            count,
            transpose,
            value,
            offset
        )
    }

    /*
        2 / (right - left)				0						0				-((right + left) / (right - left))
        0						2 / (top - bottom)				0				-((top + bottom) / (top - bottom))
        0								0				-2 / (far - near)		-((far + near) / (far - near))
        0								0						0								1
    */


}