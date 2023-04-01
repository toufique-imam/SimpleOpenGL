package com.retroapp.airhockey.airhockey.objects

import android.opengl.GLES20
import com.retroapp.airhockey.airhockey.util.ShaderHelper

class Mallet {
    private val vertexArray = VertexArray(vertexData)
    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.getPositionAttributeLocation(),
            positionComponentCount,
            stride
        )
        vertexArray.setVertexAttribPointer(
            positionComponentCount,
            colorProgram.getTextureCoordinatesAttributeLocation(),
            colorCoordinatesComponentCount,
            stride
        )
    }
    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2)
    }
    companion object {
        const val positionComponentCount = 2
        const val colorCoordinatesComponentCount = 3
        const val stride =
            (positionComponentCount + colorCoordinatesComponentCount) * ShaderHelper.bytesPerFloat

        val vertexData = floatArrayOf(
            // Order of coordinates: X, Y, R, G, B
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
        )
    }
}