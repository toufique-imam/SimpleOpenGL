package com.retroapp.airhockey.airhockey.programs

import android.content.Context
import android.opengl.GLES20.*
import com.retroapp.airhockey.R

class TextureShaderProgram(context: Context) :
    ShaderProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {
    // Uniform locations
    private val uMatrixLocation = glGetUniformLocation(program, uMatrix)
    private val uTextureUnitLocation = glGetUniformLocation(program, uTextureUnit)

    // Attribute locations
    private val aPositionLocation = glGetAttribLocation(program, aPosition)
    private val aTextureCoordinatesLocation = glGetAttribLocation(program, aTextureCoordinates)

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0)

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId)

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation(): Int {
        return aTextureCoordinatesLocation
    }
}