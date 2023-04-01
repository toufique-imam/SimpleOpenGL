package com.retroapp.airhockey.airhockey

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.opengl.Matrix.*
import com.retroapp.airhockey.R
import com.retroapp.airhockey.airhockey.objects.Mallet
import com.retroapp.airhockey.airhockey.objects.Table
import com.retroapp.airhockey.airhockey.programs.ColorShaderProgram
import com.retroapp.airhockey.airhockey.programs.TextureShaderProgram
import com.retroapp.airhockey.airhockey.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.PI


class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    lateinit var table : Table
    lateinit var mallet : Mallet
    lateinit var textureShaderProgram : TextureShaderProgram
    lateinit var colorShaderProgram : ColorShaderProgram
    private var texture1 = 0
    private var texture2 = 0

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0f,0f,0f,0f)
        table = Table()
        mallet = Mallet()
        textureShaderProgram = TextureShaderProgram(context)
        colorShaderProgram = ColorShaderProgram(context)

        texture1 = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
        texture2 = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)

    }
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        val aspectRatio =
            width.toFloat() / height.toFloat() // calculate the aspect ratio of the screen
        val fieldOfViewDegrees = 75.0f // set the field of view in degrees
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

    override fun onDrawFrame(p0: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        textureShaderProgram.useProgram()
        textureShaderProgram.setUniforms(projectionMatrix, texture1, texture2)
        textureShaderProgram.setColor(1f,1f,1f,1f)

        table.bindData(textureShaderProgram)
        table.draw()

        colorShaderProgram.useProgram()
        colorShaderProgram.setUniforms(projectionMatrix)
        mallet.bindData(colorShaderProgram)
        mallet.draw()
    }

}