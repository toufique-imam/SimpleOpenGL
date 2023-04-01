package com.retroapp.airhockey.airhockey.objects

import android.opengl.GLES20
import com.retroapp.airhockey.airhockey.programs.TextureShaderProgram
import com.retroapp.airhockey.airhockey.util.ShaderHelper

class Table {
    private val vertexArray = VertexArray(vertexData)
    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            textureProgram.getPositionAttributeLocation(),
            positionComponentCount,
            stride
        )
        vertexArray.setVertexAttribPointer(
            positionComponentCount,
            textureProgram.getTextureCoordinatesAttributeLocation(),
            textureCoordinatesComponentCount,
            stride
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
    }

    companion object {
        const val positionComponentCount = 2
        const val textureCoordinatesComponentCount = 2
        const val stride =
            (positionComponentCount + textureCoordinatesComponentCount) * ShaderHelper.bytesPerFloat

        val vertexData = floatArrayOf(
            //order of coordinates: X, Y, S, T
            // Triangle Fan
            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f
        )
    }
}