package com.carlospinan.airhockeywithbettermallets.objects

import com.carlospinan.airhockeywithbettermallets.data.VertexArray
import com.carlospinan.airhockeywithbettermallets.programs.ColorShaderProgram
import com.carlospinan.airhockeywithbettermallets.utilities.Cylinder
import com.carlospinan.airhockeywithbettermallets.utilities.Point


private const val POSITION_COMPONENT_COUNT = 3

class Puck(
    radius: Float,
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