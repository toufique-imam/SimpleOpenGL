package com.retroapp.airhockey.airhockey.programs

import android.content.Context
import android.opengl.GLES20.*
import com.retroapp.airhockey.R

class TextureShaderProgram(context: Context) :
    ShaderProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {
    // Uniform locations
    private val uMatrixLocation = glGetUniformLocation(program, uMatrix)
    private val uTextureUnitLocation1 = glGetUniformLocation(program, uTextureUnit1)
    private val uTextureUnitLocation2 = glGetUniformLocation(program, uTextureUnit2)
    private val uShaderColorLocation = glGetUniformLocation(program, uShaderColor)

    // Attribute locations
    private val aPositionLocation = glGetAttribLocation(program, aPosition)
    private val aTextureCoordinatesLocation = glGetAttribLocation(program, aTextureCoordinates)

    fun setUniforms(matrix: FloatArray, textureId1: Int, textureId2: Int) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0)

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId1)

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation1, 0)


        // Set the active texture unit to texture unit 1.
        glActiveTexture(GL_TEXTURE1)

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId2)

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 1.
        glUniform1i(uTextureUnitLocation2, 1)
    }

    fun setColor(r: Float, g: Float, b: Float, a: Float) {
        glUniform4f(uShaderColorLocation, r, g, b, a)
    }
//    fun setUniforms

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation(): Int {
        return aTextureCoordinatesLocation
    }
}