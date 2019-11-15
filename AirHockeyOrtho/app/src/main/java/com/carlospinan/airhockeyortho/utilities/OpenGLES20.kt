package com.carlospinan.airhockeyortho.utilities

import android.opengl.GLES20.*
import android.opengl.Matrix
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

    fun gl2GetUniformLocation(program: Int, name: String): Int {
        return glGetUniformLocation(program, name)
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

    fun gl2Uniform4f(
        location: Int = 0,
        red: Float = 0.0f,
        green: Float = 0.0f,
        blue: Float = 0.0f,
        alpha: Float = 0.0f
    ) {
        glUniform4f(location, red, green, blue, alpha)
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

    fun gl2DrawArrays(
        mode: Int,
        first: Int,
        count: Int
    ) {
        glDrawArrays(mode, first, count)
    }

    /*
        2 / (right - left)				0						0				-((right + left) / (right - left))
        0						2 / (top - bottom)				0				-((top + bottom) / (top - bottom))
        0								0				-2 / (far - near)		-((far + near) / (far - near))
        0								0						0								1
    */
    /**
     * orthoM(float[] m, int mOffset, float left, float right, float bottom, float top, float near, float far)
     *
     * float[] m -> The destination array—this array’s length should be at least sixteen elements so it can store the orthographic projection matrix.
     * int mOffset -> The offset into m into which the result is written
     * float left -> The minimum range of the x-axis
     * float right -> The maximum range of the x-axis
     * float bottom -> The minimum range of the y-axis
     * float top -> The maximum range of the y-axis
     * float near -> The minimum range of the z-axis
     * float far -> The maximum range of the z-axis
     */
    fun orthographic(
        destinationArray: FloatArray,
        mOffset: Int,
        left: Float,
        right: Float,
        bottom: Float,
        top: Float,
        near: Float,
        far: Float
    ) {
        Matrix.orthoM(
            destinationArray,
            mOffset,
            left,
            right,
            bottom,
            top,
            near,
            far
        )
    }

    fun perspectiveM(
        destinationArray: FloatArray,
        offset: Int,
        fovY: Float,
        aspect: Float,
        zNear: Float,
        zFar: Float
    ) {
        Matrix.perspectiveM(
            destinationArray,
            offset,
            fovY,
            aspect,
            zNear,
            zFar
        )
    }


}