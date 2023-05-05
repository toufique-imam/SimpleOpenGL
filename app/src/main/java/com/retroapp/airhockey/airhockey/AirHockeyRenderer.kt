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
import com.retroapp.airhockey.airhockey.util.Geometry
import com.retroapp.airhockey.airhockey.util.Geometry.Companion.Plane
import com.retroapp.airhockey.airhockey.util.Geometry.Companion.Point
import com.retroapp.airhockey.airhockey.util.Geometry.Companion.Ray
import com.retroapp.airhockey.airhockey.util.Geometry.Companion.Sphere
import com.retroapp.airhockey.airhockey.util.Geometry.Companion.Vector
import com.retroapp.airhockey.airhockey.util.Geometry.Companion.intersects
import com.retroapp.airhockey.airhockey.util.Geometry.Companion.vectorBetween
import com.retroapp.airhockey.airhockey.util.LoggerConfig
import com.retroapp.airhockey.airhockey.util.MatrixHelper
import com.retroapp.airhockey.airhockey.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min


class AirHockeyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)
    private var initVPM = false

    private var table = Table()
    private var mallet: Mallet = Mallet(0.08f, 0.15f, 32)
    private var puck: Puck = Puck(0.06f, 0.02f, 32)
    private lateinit var textureShaderProgram: TextureShaderProgram
    private lateinit var colorShaderProgram: ColorShaderProgram
    private var texture = 0

    private var malletPressed = false
    private var blueMalletPosition = Point(0f, mallet.height / 2f, 0.4f)
    private var prevBlueMalletPosition = Point(0f, mallet.height / 2f, 0.4f)
    private var puckPosition = Point(0f, puck.height / 2f, 0f)
    private var puckVector = Vector(0f, 0f, 0f)
    private val invertedViewProjectionMatrix = FloatArray(16)

    private val leftBound = -0.5f
    private val rightBound = 0.5f
    private val farBound = -0.8f
    private val nearBound = 0.8f

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)
        textureShaderProgram = TextureShaderProgram(context)
        colorShaderProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)

    }

    fun handleTouchPress(normalizedX: Float, normalizedY: Float) {
        val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
        val malletBoundingSphere = Sphere(
            Point(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z),
            mallet.height / 2f
        )
        malletPressed = intersects(malletBoundingSphere, ray)
    }


    fun handleTouchDrag(normalizedX: Float, normalizedY: Float) {
        if (malletPressed) {
            val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
            // Define a plane representing our air hockey table.
            val plane = Plane(Point(0f, 0f, 0f), Vector(0f, 1f, 0f))
            // Find out where the touched point intersects the plane
            // representing our table. We'll move the mallet along this plane.
            val touchedPoint: Point = Geometry.intersectionPoint(ray, plane)
            prevBlueMalletPosition = blueMalletPosition
            blueMalletPosition = Point(
                clamp(touchedPoint.x, leftBound + mallet.radius, rightBound - mallet.radius),
                mallet.height / 2f,
                clamp(touchedPoint.z, 0f + mallet.radius, nearBound - mallet.radius)
            )
            val distance = vectorBetween(blueMalletPosition, puckPosition).length()
            if (distance < (puck.radius + mallet.radius)) {
                //calculate the intersect point on puck
                val intersectPointOnPuck =
                    Geometry.findPointInLine(puckPosition, blueMalletPosition, puck.radius)
                val angle = Math.toDegrees(
                    atan2(
                        intersectPointOnPuck.x - puckPosition.x,
                        intersectPointOnPuck.z - puckPosition.z
                    ).toDouble()
                )
                LoggerConfig.e("Check", "angle >> $angle")
                LoggerConfig.e(
                    "Check",
                    "intersectPointOnPuck >> ${intersectPointOnPuck.x},${intersectPointOnPuck.y},${intersectPointOnPuck.z}"
                )
                LoggerConfig.e(
                    "Check",
                    "puckPosition >> ${puckPosition.x},${puckPosition.y},${puckPosition.z}"
                )
                puckVector = vectorBetween(prevBlueMalletPosition, blueMalletPosition)

                LoggerConfig.e(
                    "Check",
                    "puckVector >> ${puckVector.x},${puckVector.y},${puckVector.z}"
                )
            }
        }
    }

    private fun clamp(value: Float, min: Float, max: Float): Float {
        return min(max, max(value, min))
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(
            projectionMatrix,
            45f,
            (width.toFloat() / height.toFloat()),
            1f,
            10f
        )
        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f)
    }

    private fun divideW(vector: FloatArray) {
        vector[0] = vector[0] / vector[3]
        vector[1] = vector[1] / vector[3]
        vector[2] = vector[2] / vector[3]
    }

    fun reset() {
        puckPosition = Point(0f, puck.radius / 2, 0f)
        puckVector = Vector(0f, 0f, 0f)
        blueMalletPosition = Point(0f, mallet.height / 2f, 0.4f)
    }

    private fun convertNormalized2DPointToRay(normalizedX: Float, normalizedY: Float): Ray {
        //ndc = normalized device coordinates
        val nearPointNdc = floatArrayOf(normalizedX, normalizedY, -1f, 1f) //z = -1, w = 1
        val farPointNdc = floatArrayOf(normalizedX, normalizedY, 1f, 1f) //z = 1, w = 1

        val nearPointWorld = FloatArray(4)
        val farPointWorld = FloatArray(4)

        multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0)
        multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0)

        //perspective divide
        divideW(nearPointWorld)
        divideW(farPointWorld)

        val nearPointRay = Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2])
        val farPointRay = Point(farPointWorld[0], farPointWorld[1], farPointWorld[2])

        return Ray(nearPointRay, vectorBetween(nearPointRay, farPointRay))
    }

    override fun onDrawFrame(p0: GL10?) {
        puckPosition = puckPosition.translate(puckVector)
        puckVector = puckVector.scale(0.99f)
        if (puckPosition.x < leftBound + puck.radius || puckPosition.x > rightBound - puck.radius) {
            puckVector = Vector(-puckVector.x, puckVector.y, puckVector.z)
            puckVector = puckVector.scale(0.9f)
        }
        if (puckPosition.z < farBound + puck.radius || puckPosition.z > nearBound - puck.radius) {
            puckVector = Vector(puckVector.x, puckVector.y, -puckVector.z)
            puckVector = puckVector.scale(0.9f)
        }
        puckPosition = Point(
            clamp(puckPosition.x, leftBound + puck.radius, rightBound - puck.radius),
            puckPosition.y,
            clamp(puckPosition.z, farBound + puck.radius, nearBound - puck.radius)
        )

        glClear(GL_COLOR_BUFFER_BIT)
        // Multiply the view and projection matrices together.
        if (!initVPM) {
            initVPM = true
            multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
            invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0)
        }
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

        positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z)
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mallet.draw()

        //draw the puck
        positionObjectInScene(puckPosition.x, puckPosition.y, puckPosition.z)
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