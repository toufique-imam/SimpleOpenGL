package com.retroapp.airhockey.airhockey.programs

import android.content.Context
import android.opengl.GLES20.*
import com.retroapp.airhockey.R

class ColorShaderProgram (context: Context): ShaderProgram(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader){

    private val uMatrixLocation = glGetUniformLocation(program, uMatrix)
    private val aPositionLocation = glGetAttribLocation(program, aPosition)
    private val aColorLocation = glGetAttribLocation(program, aColor)

    fun setUniforms(matrix: FloatArray){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }
    fun getPositionAttributeLocation(): Int{
        return aPositionLocation
    }
    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }
}