package com.retroapp.airhockey.airhockey

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.retroapp.airhockey.R
import com.retroapp.airhockey.airhockey.util.ShaderHelper
import com.retroapp.airhockey.airhockey.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val positionComponentCount = 2
    private val uColor = "u_Color"
    private val aPosition = "a_Position"
    private var aPositionLocation: Int = 0
    private var uColorLocation: Int = 0
    private var tableVertices = floatArrayOf(
        //triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,
        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,
        // Line 1
        -0.5f, 0f,
        0.5f, 0f,
        // Mallets
        0f, -0.25f,
        0f, 0.25f,
        //triangle 3
        -0.6f, -0.6f,
        0.6f, 0.6f,
        -0.6f, 0.6f,
        // Triangle 4
        -0.6f, -0.6f,
        0.6f, -0.6f,
        0.6f, 0.6f,
    )


    private val vertexData = ShaderHelper.toFloatBuffer(tableVertices)
    private var program: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        //Reading in the Shader Code
        val vertexShaderSource = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_vertex_shader)
        val fragmentShaderSource = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_fragment_shader)
        //Compiling the Shaders from Our Renderer Class
        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)
        //Linking Shaders Together into an OpenGL Program
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)
        //Validate Our OpenGL Program Object
        ShaderHelper.validateProgram(program)
        glUseProgram(program)
        //Getting the Location of a Uniform
        uColorLocation = glGetUniformLocation(program, uColor)
        //Getting the Location of an Attribute
        aPositionLocation = glGetAttribLocation(program, aPosition)
        //Associating an Array of Vertex Data with an Attribute
        vertexData.position(0)
        glVertexAttribPointer(aPositionLocation, positionComponentCount, GL_FLOAT,false,0,vertexData)
        //Enabling the Vertex Array
        glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        // Draw the second table as border
        glUniform4f(uColorLocation, 0.5f, 0.5f, 0.5f, 1.0f)
        glDrawArrays(GL_TRIANGLES, 10, 6)
        //Drawing the Table
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        glDrawArrays(GL_TRIANGLES, 0,6)
        //Drawing the Dividing Line
        glUniform4f(uColorLocation, 1.0f , 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_LINES,6,2)

        // Draw the first mallet blue.
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        glDrawArrays(GL_POINTS, 8, 1)

        // Draw the second mallet red.
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 9, 1)
    }
}