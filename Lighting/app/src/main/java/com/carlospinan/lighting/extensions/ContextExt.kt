package com.carlospinan.lighting.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLUtils
import android.opengl.GLUtils.texImage2D
import com.carlospinan.lighting.utilities.OpenGLES20
import com.carlospinan.lighting.utilities.log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

fun Context.loadCubeMap(cubeResources: IntArray): Int {
    val textureObjectIds = IntArray(1)
    glGenTextures(1, textureObjectIds, 0)

    if (textureObjectIds[0] == 0) {
        log("Could not generate a new OpenGL texture object.")
        return 0
    }

    val options = BitmapFactory.Options().apply {
        inScaled = false
    }

    val cubeBitmaps = mutableListOf<Bitmap>()
    for (i in 0 until 6) {
        cubeBitmaps.add(
            BitmapFactory.decodeResource(
                resources,
                cubeResources[i],
                options
            )
        )
        if (cubeBitmaps[i] == null) {
            log("Resource ID ${cubeResources[i]} could not be decoded.")
            glDeleteTextures(1, textureObjectIds, 0)
            return 0
        }
    }

    glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0])

    glTexParameteri(
        GL_TEXTURE_CUBE_MAP,
        GL_TEXTURE_MIN_FILTER,
        GL_LINEAR
    )

    glTexParameteri(
        GL_TEXTURE_CUBE_MAP,
        GL_TEXTURE_MAG_FILTER,
        GL_LINEAR
    )

    texImage2D(
        GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
        0,
        cubeBitmaps[0],
        0
    )

    texImage2D(
        GL_TEXTURE_CUBE_MAP_POSITIVE_X,
        0,
        cubeBitmaps[1],
        0
    )

    texImage2D(
        GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
        0,
        cubeBitmaps[2],
        0
    )

    texImage2D(
        GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
        0,
        cubeBitmaps[3],
        0
    )

    texImage2D(
        GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,
        0,
        cubeBitmaps[4],
        0
    )

    texImage2D(
        GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
        0,
        cubeBitmaps[5],
        0
    )

    glBindTexture(GL_TEXTURE_2D, 0)

    for (bitmap in cubeBitmaps) {
        bitmap.recycle()
    }

    return textureObjectIds[0]
}

fun Context.readTextFileFromResource(resourceId: Int): String {
    val body = StringBuilder()
    this.apply {
        val stream = resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(stream)
        val bufferedReader = BufferedReader(inputStreamReader as Reader?)
        var newLine: String? = bufferedReader.readLine()
        while (newLine != null) {
            body.append(newLine).append("\n")
            newLine = bufferedReader.readLine()
        }
    }
    return body.toString()
}

/**
 * Loads a texture from a resource ID, returning the OpenGL ID for that
 * texture. Returns 0 if the load failed.
 *
 * @param resourceId
 * @return
 */
fun Context.loadTexture(resourceId: Int): Int {
    val textureObjectIds = IntArray(1)
    OpenGLES20.gl2GenTextures(1, textureObjectIds)

    if (textureObjectIds[0] == 0) {
        log("Could not generate a new OpenGL texture object.")
        return 0
    }
    val options = BitmapFactory.Options().apply {
        inScaled = false
    }
    val bitmap = BitmapFactory.decodeResource(
        resources,
        resourceId,
        options
    )
    if (bitmap == null) {
        log("Resource ID $resourceId could not be decoded.")
        glDeleteTextures(
            1,
            textureObjectIds,
            0
        )
        return 0
    }
    glBindTexture(
        GL_TEXTURE_2D,
        textureObjectIds[0]
    )

    /*
    We set each filter with a call to glTexParameteri():
    GL_TEXTURE_MIN_FILTER refers to minification, while GL_TEXTURE_MAG_FILTER refers to magnification.
    For minification, we select GL_LINEAR_MIPMAP_LINEAR, which tells OpenGL
    to use trilinear filtering. We set the magnification filter to GL_LINEAR,
    which tells OpenGL to use bilinear filtering.
     */
    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MIN_FILTER,
        GL_LINEAR_MIPMAP_LINEAR
    )

    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MAG_FILTER,
        GL_LINEAR
    )

    /*
        This call tells OpenGL to read in the bitmap data defined by
        bitmap and copy it over into the texture object that is currently bound.
     */
    GLUtils.texImage2D(
        GL_TEXTURE_2D,
        0,
        bitmap,
        0
    )
    bitmap.recycle()
    // Generating mipmaps is also a cinch. We can tell OpenGL to generate
    // all of the necessary levels with a quick call to glGenerateMipmap():
    glGenerateMipmap(GL_TEXTURE_2D)

    /*
        Now that we’ve finished loading the texture, a good practice is to then unbind from the texture
        so that we don’t accidentally make further changes to this texture with other texture calls
     */
    glBindTexture(GL_TEXTURE_2D, 0)

    return textureObjectIds[0]
}