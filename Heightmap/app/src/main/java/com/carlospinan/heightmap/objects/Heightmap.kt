package com.carlospinan.heightmap.objects

import android.graphics.Bitmap

class Heightmap(
    private val bitmap: Bitmap
) {

    private val width = bitmap.width
    private val height = bitmap.height
    private val numElements = calculateNumElements()

    init {
        if (width * height > 65536) {
            "Heightmap is too large for the index buffer."
        }
    }

    private fun calculateNumElements(): Int {
        // There should be 2 triangles for every group of 4 vertices, so a
        // heightmap of, say, 10x10 pixels would have 9x9 groups, with 2
        // triangles per group and 3 vertices per triangle for a total of (9 x 9
        // x 2 x 3) indices.
        return (width - 1) * (height - 1) * 2 * 3
    }

}