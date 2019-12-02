package com.carlospinan.airhockeywithbettermallets.objects

import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.GLES20.GL_TRIANGLE_STRIP
import com.carlospinan.airhockeywithbettermallets.utilities.Circle
import com.carlospinan.airhockeywithbettermallets.utilities.Cylinder
import com.carlospinan.airhockeywithbettermallets.utilities.OpenGLES20
import com.carlospinan.airhockeywithbettermallets.utilities.Point
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val FLOATS_PER_VERTEX = 3

class ObjectBuilder(sizeInVertices: Int) {

    private var offset = 0
    private val vertexData = FloatArray(sizeInVertices * FLOATS_PER_VERTEX)
    private val drawList = mutableListOf<DrawCommand>()

    companion object {

        fun createPuck(puck: Cylinder, numPoints: Int): GeneratedData {
            val size = sizeOfCircleInVertices(numPoints) +
                    sizeOfOpenCylinderInVertices(numPoints)

            val objectBuilder = ObjectBuilder(size)

            val puckTop = Circle(
                puck.center.translateY(puck.height / 2f),
                puck.radius
            )

            objectBuilder.appendCircle(puckTop, numPoints)
            objectBuilder.appendOpenCylinder(puck, numPoints)

            return objectBuilder.build()

        }

        fun createMallet(
            center: Point,
            radius: Float,
            height: Float,
            numPoints: Int
        ): GeneratedData {
            val size = sizeOfCircleInVertices(numPoints) * 2 +
                    sizeOfOpenCylinderInVertices(numPoints) * 2

            val objectBuilder = ObjectBuilder(size)

            // First, generate the mallet base.
            val baseHeight = height * 0.25f

            val baseCircle = Circle(
                center.translateY(-baseHeight / 2f),
                radius
            )

            val baseCylinder = Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f),
                radius,
                baseHeight
            )

            objectBuilder.appendCircle(baseCircle, numPoints)
            objectBuilder.appendOpenCylinder(baseCylinder, numPoints)

            val handleHeight = height * 0.75f
            val handleRadius = radius / 3f

            val handleCircle = Circle(
                center.translateY(height * 0.5f),
                handleRadius
            )

            val handleCylinder = Cylinder(
                handleCircle.center.translateY(-handleHeight / 2f),
                handleRadius,
                handleHeight
            )

            objectBuilder.appendCircle(handleCircle, numPoints)
            objectBuilder.appendOpenCylinder(handleCylinder, numPoints)

            return objectBuilder.build()

        }

        private fun sizeOfCircleInVertices(numPoints: Int): Int {
            return 1 + (numPoints + 1)
        }

        private fun sizeOfOpenCylinderInVertices(numPoints: Int): Int {
            return (numPoints + 1) * 2
        }
    }

    private fun appendOpenCylinder(cylinder: Cylinder, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfOpenCylinderInVertices(numPoints)

        val yStart = cylinder.center.y - (cylinder.height / 2f)
        val yEnd = cylinder.center.y + (cylinder.height / 2f)

        for (i in 0..numPoints) {
            val angleInRadians = (i.toFloat() / numPoints.toFloat()) * PI * 2f

            val xPosition = (cylinder.center.x + cylinder.radius * cos(angleInRadians)).toFloat()
            val zPosition = (cylinder.center.z + cylinder.radius * sin(angleInRadians)).toFloat()

            vertexData[offset++] = xPosition
            vertexData[offset++] = yStart
            vertexData[offset++] = zPosition

            vertexData[offset++] = xPosition
            vertexData[offset++] = yEnd
            vertexData[offset++] = zPosition
        }

        drawList.add(
            object : DrawCommand {
                override fun draw() {
                    OpenGLES20.gl2DrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices)
                }

            }
        )

    }

    private fun appendCircle(circle: Circle, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfCircleInVertices(numPoints)

        vertexData[offset++] = circle.center.x
        vertexData[offset++] = circle.center.y
        vertexData[offset++] = circle.center.z

        // Fan around center point. <= is used because we want to generate
        // the point at the starting angle twice to complete the fan.
        for (i in 0..numPoints) {
            val angleInRadians = (i.toFloat() / numPoints.toFloat()) * PI * 2f

            vertexData[offset++] = (circle.center.x + circle.radius * cos(angleInRadians)).toFloat()
            vertexData[offset++] = circle.center.y
            vertexData[offset++] = (circle.center.z + circle.radius * sin(angleInRadians)).toFloat()
        }

        drawList.add(
            object : DrawCommand {
                override fun draw() {
                    OpenGLES20.gl2DrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices)
                }

            }
        )

    }

    private fun build(): GeneratedData {
        return GeneratedData(vertexData, drawList)
    }

    interface DrawCommand {
        fun draw()
    }

    class GeneratedData(
        val vertexData: FloatArray,
        val drawList: List<DrawCommand>
    )

}