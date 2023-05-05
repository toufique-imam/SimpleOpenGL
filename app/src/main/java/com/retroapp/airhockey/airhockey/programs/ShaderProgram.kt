package com.retroapp.airhockey.airhockey.programs

import android.content.Context
import android.opengl.GLES20
import com.retroapp.airhockey.airhockey.util.ShaderHelper
import com.retroapp.airhockey.airhockey.util.TextResourceReader

open class ShaderProgram(
    context: Context,
    vertexShaderResourceId: Int,
    fragmentShaderResourceId: Int
) {
    // Compile the shaders and link the program.
    protected val program: Int = ShaderHelper.buildProgram(
        TextResourceReader.readTextFileFromResource(
            context,
            vertexShaderResourceId
        ), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
    )

    fun useProgram() {
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program)
    }

    companion object {
        // Uniform constants
        const val uMatrix = "u_Matrix"
        const val uTextureUnit = "u_TextureUnit"

        // Attribute constants
        const val aPosition = "a_Position"
        const val aColor = "a_Color"
        const val uColor = "u_Color"
        const val aTextureCoordinates = "a_TextureCoordinates"
        const val uTextureUnit1 = "u_TextureUnit1"
        const val uTextureUnit2 = "u_TextureUnit2"
        const val uShaderColor = "u_ShaderColor"
    }
}