package com.retroapp.airhockey.airhockey.programs

import android.content.Context
import android.opengl.GLES20
import com.retroapp.airhockey.airhockey.util.ShaderHelper
import com.retroapp.airhockey.airhockey.util.TextResourceReader
import org.w3c.dom.Text

open class ShaderProgram(context: Context, vertexShaderResourceId: Int, fragmentShaderResourceId: Int) {
    // Compile the shaders and link the program.
    protected val program: Int = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId))

    fun useProgram(){
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program)
    }
    companion object {
        // Uniform constants
        const val uMatrix = "u_Matrix"
        const val uTextureUnit = "u_TextureUnit"

        // Attribute constants
        const val aPosition = "a_Position"
        val aColor = "a_Color"
        const val aTextureCoordinates = "a_TextureCoordinates"
    }
}