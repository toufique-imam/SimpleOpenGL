package com.retroapp.airhockey.airhockey

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix.orthoM
import com.retroapp.airhockey.R
import com.retroapp.airhockey.airhockey.util.LoggerConfig
import com.retroapp.airhockey.airhockey.util.ShaderHelper
import com.retroapp.airhockey.airhockey.util.TextResourceReader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val positionComponentCount = 2
    private val aPosition = "a_Position"
    private val uMatrix = "u_Matrix"
    private var aPositionLocation: Int = 0

    private val colorComponentCount = 3
    private val aColor = "a_Color"
    private var aColorLocation = 0
    private val stride = (positionComponentCount + colorComponentCount) * ShaderHelper.bytesPerFloat

    private val tableVerticesWithTriangles = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B

        // Triangle Fan
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 1f, 0f, 0f,

        // Mallets
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
    )
    private val projectionMatrix = FloatArray(16)
    private var uMatrixLocation = 0


    private val vertexData = ShaderHelper.toFloatBuffer(tableVerticesWithTriangles)
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
        aColorLocation = glGetAttribLocation(program, aColor)
        //Getting the Location of an Attribute
        aPositionLocation = glGetAttribLocation(program, aPosition)
        uMatrixLocation = glGetUniformLocation(program, uMatrix)
        //Associating an Array of Vertex Data with an Attribute
        vertexData.position(0)
        glVertexAttribPointer(
            aPositionLocation,
            positionComponentCount,
            GL_FLOAT,
            false,
            stride,
            vertexData
        )
        //Enabling the Vertex Array
        glEnableVertexAttribArray(aPositionLocation)

        vertexData.position(positionComponentCount)
        glVertexAttribPointer(
            aColorLocation,
            colorComponentCount,
            GL_FLOAT,
            false,
            stride,
            vertexData
        )
        glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        val aspectRatio: Float
        if (width > height) {
            aspectRatio = (width.toFloat() / height.toFloat())
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            aspectRatio = (height.toFloat() / width.toFloat())
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
        LoggerConfig.v("aspectRatio", aspectRatio.toString())
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)
        //Drawing the Table
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)

        // Draw the center dividing line.
        glDrawArrays(GL_LINES, 6, 2)

        // Draw the first mallet.
        glDrawArrays(GL_POINTS, 8, 1)

        // Draw the second mallet.
        glDrawArrays(GL_POINTS, 9, 1)
    }
}