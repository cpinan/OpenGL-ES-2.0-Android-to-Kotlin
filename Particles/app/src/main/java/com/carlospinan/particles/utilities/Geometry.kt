package com.carlospinan.particles.utilities


import kotlin.math.sqrt

object Geometry {

    fun vectorBetween(from: Point, to: Point): Vector {
        return Vector(
            to.x - from.x,
            to.y - from.y,
            to.z - from.z
        )
    }

}

data class Vector(
    val x: Float,
    val y: Float,
    val z: Float
) {

    companion object {
        fun zero(): Vector = Vector(0f, 0f, 0f)
    }

    fun length(): Float {
        return sqrt(x * x + y * y + z * z)
    }

    fun crossProduct(other: Vector): Vector {
        return Vector(
            (y * other.z) - (z * other.y),
            (z * other.x) - (x * other.z),
            (x * other.y) - (y * other.x)
        )
    }

    fun dotProduct(other: Vector): Float {
        return x * other.x + y * other.y + z * other.z
    }

    fun scale(scaleFactor: Float): Vector {
        return Vector(x * scaleFactor, y * scaleFactor, z * scaleFactor)
    }
}

data class Point(
    val x: Float,
    val y: Float,
    val z: Float
) {

    companion object {
        fun zero(): Point = Point(0f, 0f, 0f)
        fun from(position: Point): Point = Point(position.x, position.y, position.z)
    }

    fun translateY(distance: Float): Point {
        return Point(x, y + distance, z)
    }

    fun translate(vector: Vector): Point {
        return Point(x + vector.x, y + vector.y, z + vector.z)
    }

}