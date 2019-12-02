package com.carlospinan.airhockeywithbettermallets.utilities

data class Point(
    val x: Float,
    val y: Float,
    val z: Float
) {

    companion object {
        fun zero(): Point = Point(0f, 0f, 0f)
    }

    fun translateY(distance: Float): Point {
        return Point(x, y + distance, z)
    }

}

data class Circle(
    val center: Point,
    val radius: Float
) {

    fun scale(scale: Float): Circle = Circle(center, radius * scale)

}

data class Cylinder(
    val center: Point,
    val radius: Float,
    val height: Float
)