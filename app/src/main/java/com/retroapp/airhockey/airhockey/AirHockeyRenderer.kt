package com.retroapp.airhockey.airhockey

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.opengl.Matrix.*
import com.retroapp.airhockey.R
import com.retroapp.airhockey.airhockey.util.LoggerConfig
import com.retroapp.airhockey.airhockey.util.MatrixHelper
import com.retroapp.airhockey.airhockey.util.ShaderHelper
import com.retroapp.airhockey.airhockey.util.TextResourceReader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.PI


class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val positionComponentCount = 4
    private val aPosition = "a_Position"
    private val uMatrix = "u_Matrix"
    private var aPositionLocation: Int = 0

    private val colorComponentCount = 3
    private val aColor = "a_Color"
    private var aColorLocation = 0
    private val stride = (positionComponentCount + colorComponentCount) * ShaderHelper.bytesPerFloat
    private val tableVerticesWithTriangles = floatArrayOf(
        // Order of coordinates: X, Y, Z, W, R, G, B

        // Triangle Fan
        0f, 0f, 0f, 1.5f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
        0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

        // Mallets
        0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
        0f, 0.4f, 0f, 1.75f, 1f, 0f, 0f
    )
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
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
        val aspectRatio =
            width.toFloat() / height.toFloat() // calculate the aspect ratio of the screen
        val fieldOfViewDegrees = 45.0f // set the field of view in degrees
        val near = 1.0f // set the near clipping plane distance
        val far = 10.0f // set the far clipping plane distance
//        perspectiveM(projectionMatrix, 0, fieldOfViewDegrees, aspectRatio, near, far)
        MatrixHelper.perspectiveM(projectionMatrix, fieldOfViewDegrees, aspectRatio, near, far)
        setIdentityM(modelMatrix, 0)
        translateM(modelMatrix, 0, 0f, 0f, -2f)
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)

        val temp = FloatArray(16)
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)
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