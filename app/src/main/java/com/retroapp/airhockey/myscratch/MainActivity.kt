package com.retroapp.airhockey.myscratch

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        gLView = MyGLSurfaceView(this);

        setContentView(gLView);
    }

    override fun onPause() {
        super.onPause()
        gLView.onPause()
    }

    override fun onResume() {
        super.onResume()
        gLView.onResume()
    }
}