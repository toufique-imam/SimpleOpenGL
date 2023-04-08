package com.retroapp.airhockey.airhockey.programs

import android.content.Context
import android.opengl.GLES20.*
import com.retroapp.airhockey.R

class ColorShaderProgram(context: Context) :
    ShaderProgram(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {

    private val uMatrixLocation = glGetUniformLocation(program, uMatrix)
    private val aPositionLocation = glGetAttribLocation(program, aPosition)
    private val uColorLocation = glGetUniformLocation(program, uColor)

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

}