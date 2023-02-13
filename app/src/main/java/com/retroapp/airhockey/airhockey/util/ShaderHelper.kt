package com.retroapp.airhockey.airhockey.util

import android.opengl.GLES20.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

class ShaderHelper {
    companion object {
        private const val TAG = "ShaderHelper"
        const val bytesPerFloat = 4

        fun compileVertexShader(shaderCode: String): Int {
            return compileShader(GL_VERTEX_SHADER, shaderCode)
        }

        fun compileFragmentShader(shaderCode: String): Int {
            return compileShader(GL_FRAGMENT_SHADER, shaderCode)
        }

        private fun compileShader(type: Int, shaderCode: String): Int {
            val shaderObjectId = glCreateShader(type)
            if (shaderObjectId == 0) {
                LoggerConfig.w(TAG, "Couldn't create new shader")
                return 0
            }
            glShaderSource(shaderObjectId, shaderCode)
            glCompileShader(shaderObjectId)

            val compileStatus = intArrayOf(1)
            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0)
            LoggerConfig.v(
                TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
                        + glGetShaderInfoLog(shaderObjectId)
            )
            if (compileStatus[0] == 0) {
                glDeleteShader(shaderObjectId)
                LoggerConfig.w(TAG, "Compilation of shader failed")
                throw RuntimeException("Compilation error of shader")
            }
            return shaderObjectId
        }

        fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
            val programObjectId = glCreateProgram()
            if (programObjectId == 0) {
                LoggerConfig.w(TAG, "Couldn't create new program")
                return 0
            }
            glAttachShader(programObjectId, vertexShaderId)
            glAttachShader(programObjectId, fragmentShaderId)
            glLinkProgram(programObjectId)

            val linkStatus = intArrayOf(1)
            glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0)
            LoggerConfig.v(
                TAG, "\"Results of linking program:\\n\"\n" +
                        "+ glGetProgramInfoLog(programObjectId)"
            )
            if (linkStatus[0] == 0) {
                glDeleteProgram(programObjectId)
                throw RuntimeException("Linking program failed")
            }
            return programObjectId
        }

        fun validateProgram(programObjectId: Int): Boolean {
            glValidateProgram(programObjectId)

            val validateStatus = intArrayOf(1)
            glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0)
            LoggerConfig.v(
                TAG,
                "Results of validating program $validateStatus[0]\nLog: ${
                    glGetProgramInfoLog(programObjectId)
                }"
            )

            return validateStatus[0] != 0
        }
        fun toFloatBuffer(array: FloatArray): FloatBuffer {
            val buffer = ByteBuffer.allocateDirect(array.size * bytesPerFloat)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().apply {
                    put(array)
                    position(0)
                }
            return buffer
        }
        fun toIntBuffer(array: IntArray): IntBuffer {
            val buffer = ByteBuffer.allocateDirect(array.size * bytesPerFloat)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer().apply {
                    put(array)
                    position(0)
                }
            return buffer
        }

        fun toFloatBuffer(array: Array<Any>): FloatBuffer {
            val floatArray = array.filterIsInstance<Number>().map { it.toFloat() }.toFloatArray()
            return toFloatBuffer(floatArray)
        }
        fun toIntBuffer(array: Array<Any>): IntBuffer {
            val intArray = array.filterIsInstance<Number>().map { it.toInt() }.toIntArray()
            return toIntBuffer(intArray)
        }
    }
}