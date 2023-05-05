package com.retroapp.airhockey.airhockey

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.retroapp.airhockey.R

class AirHockeyActivity : AppCompatActivity() {
    private lateinit var gLView: AirHockeySurfaceView
    private lateinit var resetButton: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gLView = findViewById(R.id.glView_render)
        resetButton = findViewById(R.id.button_reset)
        resetButton.setOnClickListener {
            gLView.reset()
        }
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