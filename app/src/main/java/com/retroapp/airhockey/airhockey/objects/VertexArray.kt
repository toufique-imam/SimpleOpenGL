package com.retroapp.airhockey.airhockey.objects

import android.opengl.GLES20.*
import com.retroapp.airhockey.airhockey.util.ShaderHelper

class VertexArray(vertexData: FloatArray) {
    private val floatBuffer = ShaderHelper.toFloatBuffer(vertexData)

    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        floatBuffer.position(dataOffset)
        glVertexAttribPointer(
            attributeLocation,
            componentCount,
            GL_FLOAT,
            false,
            stride,
            floatBuffer
        )
        glEnableVertexAttribArray(attributeLocation)

        floatBuffer.position(0)
    }
}