package com.carlospinan.airhockeytouch.objects

import com.carlospinan.airhockeytouch.data.VertexArray
import com.carlospinan.airhockeytouch.programs.ColorShaderProgram
import com.carlospinan.airhockeytouch.utilities.Cylinder
import com.carlospinan.airhockeytouch.utilities.Point

private const val POSITION_COMPONENT_COUNT = 3

class Puck(
    val radius: Float,
    val height: Float,
    numPointsAroundPuck: Int
) {

    private var vertexArray: VertexArray
    private var drawList: List<ObjectBuilder.DrawCommand>

    init {
        val generatedData = ObjectBuilder.createPuck(
            Cylinder(
                Point.zero(),
                radius,
                height
            ),
            numPointsAroundPuck
        )
        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }

    fun bindData(colorShaderProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorShaderProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            0
        )
    }

    fun draw() {
        drawList.forEach {
            it.draw()
        }
    }

}