package com.carlospinan.airhockey2.utilities

import android.opengl.GLES20.*
import java.nio.Buffer

object OpenGLES20 {

    // CREATIONAL

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

    fun gl2ClearColor(
        red: Float = 0.0f,
        green: Float = 0.0f,
        blue: Float = 0.0f,
        alpha: Float = 1.0f
    ) {
        glClearColor(red, green, blue, alpha)
    }

    fun gl2ViewPort(
        x: Int = 0,
        y: Int = 0,
        width: Int,
        height: Int
    ) {
        glViewport(x, y, width, height)
    }

    fun gl2Clear(value: Int = GL_COLOR_BUFFER_BIT) {
        glClear(value)
    }

    fun gl2GetAttribLocation(program: Int, name: String): Int {
        return glGetAttribLocation(program, name)
    }

    fun gl2VertexAttribPointer(
        index: Int = 0,
        size: Int = 0,
        type: Int = 0,
        normalized: Boolean = false,
        stride: Int = 0,
        pointer: Buffer
    ) {
        glVertexAttribPointer(
            index,
            size,
            type,
            normalized,
            stride,
            pointer
        )
    }

    fun gl2EnableVertexAttribArray(index: Int) {
        glEnableVertexAttribArray(index)
    }

    fun gl2DrawArrays(
        mode: Int,
        first: Int,
        count: Int
    ) {
        glDrawArrays(mode, first, count)
    }

}