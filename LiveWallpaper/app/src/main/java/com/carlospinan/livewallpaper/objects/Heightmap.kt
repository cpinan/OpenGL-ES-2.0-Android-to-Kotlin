package com.carlospinan.livewallpaper.objects

import android.graphics.Bitmap
import android.graphics.Color
import android.opengl.GLES20.*
import androidx.core.math.MathUtils
import com.carlospinan.livewallpaper.common.BYTES_PER_FLOAT
import com.carlospinan.livewallpaper.data.IndexBuffer
import com.carlospinan.livewallpaper.data.VertexBuffer
import com.carlospinan.livewallpaper.programs.HeightmapShaderProgram
import com.carlospinan.livewallpaper.utilities.Geometry
import com.carlospinan.livewallpaper.utilities.Point

private const val NORMAL_COMPONENT_COUNT = 3

private const val POSITION_COMPONENT_COUNT = 3

private const val TOTAL_COMPONENT_COUNT =
    POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT

private const val STRIDE =
    (POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT) * BYTES_PER_FLOAT

class Heightmap(
    bitmap: Bitmap
) {

    private val width = bitmap.width
    private val height = bitmap.height

    private val numElements = calculateNumElements()
    private val vertexBuffer: VertexBuffer
    private val indexBuffer: IndexBuffer

    init {
        if (width * height > 65536) {
            "Heightmap is too large for the index buffer."
        }
        vertexBuffer = VertexBuffer(loadBitmapData(bitmap))
        indexBuffer = IndexBuffer(createIndexData())
    }

    private fun calculateNumElements(): Int {
        // There should be 2 triangles for every group of 4 vertices, so a
        // heightmap of, say, 10x10 pixels would have 9x9 groups, with 2
        // triangles per group and 3 vertices per triangle for a total of (9 x 9
        // x 2 x 3) indices.
        return (width - 1) * (height - 1) * 2 * 3
    }

    /**
     * Copy the heightmap data into a vertex buffer object.
     */
    private fun loadBitmapData(bitmap: Bitmap): FloatArray {
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        bitmap.recycle()

        val heightmapVertices = FloatArray(width * height * TOTAL_COMPONENT_COUNT)
        var offset = 0
        for (row in 0 until height) {
            for (col in 0 until width) {
                // The heightmap will lie flat on the XZ plane and centered
                // around (0, 0), with the bitmap width mapped to X and the
                // bitmap height mapped to Z, and Y representing the height. We
                // assume the heightmap is grayscale, and use the value of the
                // red color to determine the height.
                val point = getPoint(pixels, row, col)

                heightmapVertices[offset++] = point.x
                heightmapVertices[offset++] = point.y
                heightmapVertices[offset++] = point.z

                val top = getPoint(pixels, row - 1, col)
                val left = getPoint(pixels, row, col - 1)
                val right = getPoint(pixels, row, col + 1)
                val bottom = getPoint(pixels, row + 1, col)

                val rightToLeft = Geometry.vectorBetween(right, left)
                val topToBottom = Geometry.vectorBetween(top, bottom)
                val normal = rightToLeft.crossProduct(topToBottom).normalize()

                heightmapVertices[offset++] = normal.x
                heightmapVertices[offset++] = normal.y
                heightmapVertices[offset++] = normal.z
            }
        }
        return heightmapVertices
    }


    /**
     * Create an index buffer object for the vertices to wrap them together into
     * triangles, creating indices based on the width and height of the
     * heightmap.
     */
    private fun createIndexData(): ShortArray {
        val indexData = ShortArray(numElements)
        var offset = 0
        for (row in 0 until height - 1) {
            for (col in 0 until width - 1) {
                // Note: The (short) cast will end up underflowing the number
                // into the negative range if it doesn't fit, which gives us the
                // right unsigned number for OpenGL due to two's complement.
                // This will work so long as the heightmap contains 65536 pixels
                // or less.
                val topLeftIndexNum = (row * width + col).toShort()
                val topRightIndexNum = (row * width + col + 1).toShort()
                val bottomLeftIndexNum = ((row + 1) * width + col).toShort()
                val bottomRightIndexNum =
                    ((row + 1) * width + col + 1).toShort()
                // Write out two triangles.
                indexData[offset++] = topLeftIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = topRightIndexNum
                indexData[offset++] = topRightIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = bottomRightIndexNum
            }
        }
        return indexData
    }

    fun bindData(heightmapProgram: HeightmapShaderProgram) {
        vertexBuffer.setVertexAttribPointer(
            0,
            heightmapProgram.positionAttributeLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )

        vertexBuffer.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT * BYTES_PER_FLOAT,
            heightmapProgram.normalAttributeLocation,
            NORMAL_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            indexBuffer.bufferId
        )

        glDrawElements(
            GL_TRIANGLES,
            numElements,
            GL_UNSIGNED_SHORT,
            0
        )

        glBindBuffer(
            GL_ELEMENT_ARRAY_BUFFER,
            0
        )
    }

    /**
     * Returns a point at the expected position given by row and col, but if the
     * position is out of bounds, then it clamps the position and uses the
     * clamped position to read the height. For example, calling with row = -1
     * and col = 5 will set the position as if the point really was at -1 and 5,
     * but the height will be set to the heightmap height at (0, 5), since (-1,
     * 5) is out of bounds. This is useful when we're generating normals, and we
     * need to read the heights of neighbouring points.
     */
    private fun getPoint(pixels: IntArray, row: Int, col: Int): Point {
        var row = row
        var col = col
        val x = col.toFloat() / (width - 1).toFloat() - 0.5f
        val z = row.toFloat() / (height - 1).toFloat() - 0.5f
        row = MathUtils.clamp(row, 0, width - 1)
        col = MathUtils.clamp(col, 0, height - 1)
        val y =
            Color.red(pixels[row * height + col]).toFloat() / 255f
        return Point(x, y, z)
    }

}