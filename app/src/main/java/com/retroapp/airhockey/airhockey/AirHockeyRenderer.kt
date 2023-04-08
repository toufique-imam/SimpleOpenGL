package com.retroapp.airhockey.airhockey

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix.*
import com.retroapp.airhockey.R
import com.retroapp.airhockey.airhockey.objects.Mallet
import com.retroapp.airhockey.airhockey.objects.Puck
import com.retroapp.airhockey.airhockey.objects.Table
import com.retroapp.airhockey.airhockey.programs.ColorShaderProgram
import com.retroapp.airhockey.airhockey.programs.TextureShaderProgram
import com.retroapp.airhockey.airhockey.util.MatrixHelper
import com.retroapp.airhockey.airhockey.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)


    private lateinit var table: Table
    private lateinit var mallet: Mallet
    private lateinit var puck: Puck
    private lateinit var textureShaderProgram: TextureShaderProgram
    private lateinit var colorShaderProgram: ColorShaderProgram
    private var texture = 0
    var rotationAngle: Float = 0f

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)
        table = Table()
        mallet = Mallet(0.08f, 0.15f, 32)
        puck = Puck(0.06f, 0.02f, 32)
        textureShaderProgram = TextureShaderProgram(context)
        colorShaderProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(
            projectionMatrix, 65f, (width.toFloat() / height.toFloat()), 1f, 10f
        )
        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    override fun onDrawFrame(p0: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        // Multiply the view and projection matrices together.
        rotationAngle = 0.01f
        rotateM(viewMatrix, 0, rotationAngle, 0f, 1f, 0f)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        //Draw the table
        positionTableInScene()
        textureShaderProgram.useProgram()
        textureShaderProgram.setColor(1f, 1f, 1f, 1f)
        textureShaderProgram.setUniforms(modelViewProjectionMatrix, texture, texture)
        table.bindData(textureShaderProgram)
        table.draw()

        //Draw the mallets
        positionObjectInScene(0f, mallet.height / 2f, -0.4f)
        colorShaderProgram.useProgram()
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorShaderProgram)
        mallet.draw()

        positionObjectInScene(0f, mallet.height / 2f, 0.4f)
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mallet.draw()

        //draw the puck
        positionObjectInScene(0f, puck.height / 2f, 0f)
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorShaderProgram)
        puck.draw()
    }

    private fun positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        setIdentityM(modelMatrix, 0)
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        setIdentityM(modelMatrix, 0)
        translateM(modelMatrix, 0, x, y, z)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

}