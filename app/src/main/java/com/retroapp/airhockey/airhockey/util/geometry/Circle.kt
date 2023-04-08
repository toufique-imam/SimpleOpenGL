package com.retroapp.airhockey.airhockey.util.geometry

class Circle(val center: Point, val radius: Float) {
    fun scale(scale: Float) : Circle {
        return Circle(center, radius*scale)
    }
}