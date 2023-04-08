package com.retroapp.airhockey.airhockey

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class AirHockeySurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: AirHockeyRenderer
//    private val touchScaleFactor: Float = 180.0f / 320f
    private var previousX: Float = 0.0f
    private var previousY: Float = 0.0f
    private var rotationAngleX: Float = 0.0f
    private var rotationAngleY: Float = 0.0f

    init {
        setEGLContextClientVersion(2)

        renderer = AirHockeyRenderer(context)
        //setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(renderer)

        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val currentX = event.x
        val currentY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Record the initial touch coordinates
                previousX = currentX
                previousY = currentY
            }
            MotionEvent.ACTION_UP -> {
                // Reset the rotation angle
                rotationAngleX = 0.0f
                rotationAngleY = 0.0f
            }
            MotionEvent.ACTION_MOVE -> {
                // Calculate the change in touch coordinates
                val deltaX = currentX - previousX
                val deltaY = currentY - previousY

                // Update the rotation angles based on touch input
                rotationAngleX += deltaY // Adjust the sensitivity to your preference
                rotationAngleY += deltaX // Adjust the sensitivity to your preference

                // Call a method to apply the rotation to your GL rendering
                applyRotation(rotationAngleX, rotationAngleY)

                // Update the previous touch coordinates for the next frame
                previousX = currentX
                previousY = currentY
            }
        }
        return true
    }

    // Method to apply rotation to your GL rendering
    private fun applyRotation(angleX: Float, angleY: Float) {
        requestRender()
    }
}