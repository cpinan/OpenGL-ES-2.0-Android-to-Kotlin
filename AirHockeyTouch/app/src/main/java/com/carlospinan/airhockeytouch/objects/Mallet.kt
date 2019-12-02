package com.carlospinan.airhockeytouch.objects

import com.carlospinan.airhockeytouch.data.VertexArray
import com.carlospinan.airhockeytouch.programs.ColorShaderProgram
import com.carlospinan.airhockeytouch.utilities.Point

private const val POSITION_COMPONENT_COUNT = 3

class Mallet(
    val radius: Float,
    val height: Float,
    numPointsAroundMallet: Int
) {

    private var vertexArray: VertexArray
    private var drawList: List<ObjectBuilder.DrawCommand>

    init {
        val generatedData = ObjectBuilder.createMallet(
            Point.zero(),
            radius,
            height,
            numPointsAroundMallet
        )

        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }

    fun draw() {
        drawList.forEach {
            it.draw()
        }
    }

    fun bindData(program: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            program.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            0
        )
    }

}