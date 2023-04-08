package com.retroapp.airhockey.airhockey.objects

import com.retroapp.airhockey.airhockey.programs.ColorShaderProgram
import com.retroapp.airhockey.airhockey.util.geometry.Cylinder
import com.retroapp.airhockey.airhockey.util.geometry.Point

class Puck(radius: Float, val height: Float, numPointsAroundPuck: Int) {
    private val generatedData =
        ObjectBuilder.createPuck(Cylinder(Point(0f, 0f, 0f), radius, height), numPointsAroundPuck)
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