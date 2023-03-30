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
        /**
         * Compiles the Vertex shader code and returns the ShaderObjectId
         *
         * @param shaderCode String, Shader code in string
         * @return shaderObjectId Int, ShaderObjectId if compilation was successful
         * @throws RuntimeException
         */
        fun compileVertexShader(shaderCode: String): Int {
            return compileShader(GL_VERTEX_SHADER, shaderCode)
        }
        /**
         * Compiles the Fragment shader code and returns the ShaderObjectId
         *
         * @param shaderCode String, Shader code in string
         * @return shaderObjectId Int, ShaderObjectId if compilation was successful
         * @throws RuntimeException
         */
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
        /**
         * Links the Vertex Shader and Fragment Shader
         *
         * @param vertexShaderId Int, VertexShaderId, which was returned from compilation
         * @param fragmentShaderId Int, FragmentShaderId, which was returned from compilation
         * @return programObjectId Int, if linking was successful
         * @throws RuntimeException
         */
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

        /**
         * validates the program
         *
         * @param programObjectId Int (Program object id)
         * @return Boolean (validation result)
         */
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
        /**
         * converts the array to FloatBuffer
         *
         * @param array FloatArray (Program object id)
         * @return FloatBuffer
         */
        fun toFloatBuffer(array: FloatArray): FloatBuffer {
            val buffer = ByteBuffer.allocateDirect(array.size * bytesPerFloat)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer().apply {
                    put(array)
                    position(0)
                }
            return buffer
        }

        /**
         * converts the array to IntBuffer
         *
         * @param array IntArray (Program object id)
         * @return FloatBuffer
         */
        fun toIntBuffer(array: IntArray): IntBuffer {
            val buffer = ByteBuffer.allocateDirect(array.size * bytesPerFloat)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer().apply {
                    put(array)
                    position(0)
                }
            return buffer
        }
        /**
         * converts the array of any type to FloatArray
         *
         * @param array Array<Any> (Program object id)
         * @return FloatBuffer
         */
        fun toFloatBuffer(array: Array<Any>): FloatBuffer {
            val floatArray = array.filterIsInstance<Number>().map { it.toFloat() }.toFloatArray()
            return toFloatBuffer(floatArray)
        }
        /**
         * converts the array of any type to IntBuffer
         *
         * @param array Array<Any> (Program object id)
         * @return FloatBuffer
         */
        fun toIntBuffer(array: Array<Any>): IntBuffer {
            val intArray = array.filterIsInstance<Number>().map { it.toInt() }.toIntArray()
            return toIntBuffer(intArray)
        }
    }
}