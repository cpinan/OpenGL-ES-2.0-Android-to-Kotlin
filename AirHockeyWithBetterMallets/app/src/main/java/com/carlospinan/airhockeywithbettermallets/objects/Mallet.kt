package com.carlospinan.airhockeywithbettermallets.objects

import com.carlospinan.airhockeywithbettermallets.data.VertexArray
import com.carlospinan.airhockeywithbettermallets.programs.ColorShaderProgram
import com.carlospinan.airhockeywithbettermallets.utilities.Point


private const val POSITION_COMPONENT_COUNT = 3

class Mallet(
    radius: Float,
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