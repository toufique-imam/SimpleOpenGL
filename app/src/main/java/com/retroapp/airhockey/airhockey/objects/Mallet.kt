package com.retroapp.airhockey.airhockey.objects

import com.retroapp.airhockey.airhockey.programs.ColorShaderProgram
import com.retroapp.airhockey.airhockey.util.Geometry.Companion.Point

class Mallet(radius: Float, val height: Float, numOfPointsAroundMallet: Int) {
    private val generatedData =
        ObjectBuilder.createMallet(Point(0f, 0f, 0f), radius, height, numOfPointsAroundMallet)
    private val vertexArray = VertexArray(generatedData.vertexData)
    private val drawList = generatedData.drawList


    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.getPositionAttributeLocation(),
            positionComponentCount,
            0
        )
    }

    fun draw() {
        for (drawCommand in drawList) {
            drawCommand.draw()
        }
    }

    companion object {
        const val positionComponentCount = 3
    }
}