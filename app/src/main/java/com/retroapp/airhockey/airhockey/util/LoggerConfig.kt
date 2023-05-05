package com.retroapp.airhockey.airhockey.util

import android.util.Log

class LoggerConfig {
    companion object {
        var ON = true
        fun e(TAG: String, message: String) {
            if (!ON) return
            Log.e(TAG, message)
        }

        fun i(TAG: String, message: String) {
            if (!ON) return
            Log.i(TAG, message)
        }

        fun w(TAG: String, message: String) {
            if (!ON) return
            Log.w(TAG, message)
        }

        fun d(TAG: String, message: String) {
            if (!ON) return
            Log.d(TAG, message)
        }

        fun v(TAG: String, message: String) {
            if (!ON) return
            Log.v(TAG, message)
        }
    }
}