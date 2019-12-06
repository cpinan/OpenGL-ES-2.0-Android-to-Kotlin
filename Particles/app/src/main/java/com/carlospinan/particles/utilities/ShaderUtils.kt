package com.carlospinan.particles.utilities


import android.opengl.GLES20.*

/**
 * compileShader()
 *      The compileShader(shaderCode) method takes in source code for a shader and the shader’s type.
 *      The type can be GL_VERTEX_SHADER for a vertex shader, or GL_FRAGMENT_SHADER for a fragment shader.
 *      If OpenGL was able to success- fully compile the shader, then this method will return the shader object ID
 *      to the calling code. Otherwise it will return zero.
 *
 * compileVertexShader()
 *      The compileVertexShader(shaderCode) method is a helper method that calls compileShader()
 *      with shader type GL_VERTEX_SHADER.
 *
 * compileFragmentShader()
 *      The compileVertexShader(shaderCode) method is a helper method that calls compileShader()
 *      with shader type GL_FRAGMENT_SHADER.
 */
object ShaderUtils {

    /**
     * If the object creation failed, we’ll return 0 to the calling code.
     * Why do we return 0 instead of throwing an exception?
     * Well, OpenGL doesn’t actually throw any exceptions internally.
     * Instead, we’ll get a return value of 0 or OpenGL will inform us of the error through glGetError(),
     * a method that lets us ask OpenGL if any of our API calls have resulted in an error.
     * We’ll follow the same convention to stay consistent.
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        val shareObjectId =
            OpenGLES20.gl2CreateShader(
                type
            )
        if (shareObjectId == 0) {
            log("Could not create a new shader.")
            return 0
        }
        /**
         * Once we have a valid shader object, we call glShaderSource(shaderObjectId, shaderCode)
         * to upload the source code. This call tells OpenGL to read in the source code defined
         * in the String shaderCode and associate it with the shader object referred to by shaderObjectId.
         */
        OpenGLES20.gl2ShaderSource(
            shareObjectId,
            shaderCode
        )

        /**
         * This tells OpenGL to compile the source code that was previously uploaded to shaderObjectId.
         */
        OpenGLES20.gl2CompileShader(
            shareObjectId
        )

        /**
         * To check whether the compile failed or succeeded,
         * we first create a new int array with a length of 1 and call it compileStatus.
         * We then call glGetShaderiv(shader- ObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0).
         * This tells OpenGL to read the compile status associated with
         * shaderObjectId and write it to the 0th element of compileStatus.
         */
        val compileStatus = IntArray(1)
        OpenGLES20.gl2GetShaderIv(
            shareObjectId,
            compileStatus
        )

        val shaderInfoLog =
            OpenGLES20.gl2GetShaderInfoLog(
                shareObjectId
            )

        log("Results of compiling source:\n$shaderCode\n:$shaderInfoLog")

        if (compileStatus[0] == 0) {

            OpenGLES20.gl2DeleteShader(
                shareObjectId
            )

            log("Compilation of shader failed.")

            return 0
        }

        return shareObjectId
    }


    private fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(
            GL_FRAGMENT_SHADER,
            shaderCode
        )
    }

    private fun compileVertexShader(shaderCode: String): Int {
        return compileShader(
            GL_VERTEX_SHADER,
            shaderCode
        )
    }

    private fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        val programObjectId =
            OpenGLES20.gl2CreateProgram()
        if (programObjectId == 0) {
            log("Could not create a new program.")
            return 0
        }

        OpenGLES20.gl2AttachShader(
            programObjectId,
            vertexShaderId
        )
        OpenGLES20.gl2AttachShader(
            programObjectId,
            fragmentShaderId
        )

        OpenGLES20.gl2LinkProgram(
            programObjectId
        )

        val shaderInfoLog =
            OpenGLES20.gl2GetShaderInfoLog(
                programObjectId
            )

        val linkStatus = IntArray(1)
        OpenGLES20.gl2GetProgramiv(
            programObjectId,
            GL_LINK_STATUS,
            linkStatus
        )

        log("Results of linking program:\n:$shaderInfoLog")

        if (linkStatus[0] == 0) {
            OpenGLES20.gl2DeleteProgram(
                programObjectId
            )
            log("Linking of program failed.")
            return 0
        }

        return programObjectId
    }

    private fun validateProgram(programObjectId: Int): Boolean {
        OpenGLES20.gl2ValidateProgram(
            programObjectId
        )

        val validateStatus = IntArray(1)
        OpenGLES20.gl2GetProgramiv(
            programObjectId,
            GL_VALIDATE_STATUS,
            validateStatus
        )

        val shaderInfoLog =
            OpenGLES20.gl2GetShaderInfoLog(
                programObjectId
            )

        log("Results of validating program ${validateStatus[0]}\nLog:$shaderInfoLog")

        return validateStatus[0] != 0
    }

    fun buildProgram(vertexShaderSource: String, fragmentShaderSource: String): Int {
        val vertexShader =
            compileVertexShader(
                vertexShaderSource
            )
        val fragmentShader =
            compileFragmentShader(
                fragmentShaderSource
            )
        val program =
            linkProgram(
                vertexShader,
                fragmentShader
            )

        validateProgram(
            program
        )

        return program
    }

}