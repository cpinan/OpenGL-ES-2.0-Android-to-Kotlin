package com.carlospinan.skybox.utilities

data class Vector(
    val x: Float,
    val y: Float,
    val z: Float
) {

    companion object

}

data class Point(
    val x: Float,
    val y: Float,
    val z: Float
) {

    companion object {
        fun zero(): Point = Point(0f, 0f, 0f)
    }

}