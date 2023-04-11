package com.retroapp.airhockey.airhockey

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class AirHockeySurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: AirHockeyRenderer
    private val touchScaleFactor: Float = 180.0f / 320f
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    init {
        setEGLContextClientVersion(2)

        renderer = AirHockeyRenderer(context)
        //setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(renderer)

        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
        if (event == null) return false
        val x: Float = event.x
        val y: Float = event.y

        val normalizedX = (event.x / width) * 2 - 1
        val normalizedY = (event.y / height) * 2 - 1

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                this.queueEvent {
                    renderer.handleTouchDrag(normalizedX, normalizedY)
                }
            }
            MotionEvent.ACTION_DOWN -> {
                this.queueEvent {
                    renderer.handleTouchPressed(normalizedX, normalizedY)
                }
            }
        }

        previousX = x
        previousY = y
        return true
    }
}