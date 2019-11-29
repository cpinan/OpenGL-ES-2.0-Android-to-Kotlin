package com.carlospinan.airhockeywithbettermallets.utilities

data class Point(
    private val x: Float,
    private val y: Float,
    private val z: Float
) {

    fun translateY(distance: Float): Point {
        return Point(x, y + distance, z)
    }

}

data class Circle(
    private val center: Point,
    private val radius: Float
) {

    fun scale(scale: Float): Circle = Circle(center, radius * scale)

}

data class Cylinder(
    private val center: Point,
    private val radius: Float,
    private val height: Float
)