package com.retroapp.airhockey.airhockey.util

import android.content.Context
import android.content.res.Resources
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class TextResourceReader {
    companion object {
        const val TAG = "TextResourceReader"

        /**
         * Returns the text from given resource, used to get the string for shader code.
         *
         * @param context Context
         * @param resourceId Int
         * @return String, Shader code
         * @throws Resources.NotFoundException
         * @throws IOException
         */
        fun readTextFileFromResource(context: Context, resourceId: Int): String {
            val body = StringBuilder()

            try {
                val inputStream = context.resources.openRawResource(resourceId)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)

                var nextLine: String? = null
                while (true) {
                    nextLine = bufferedReader.readLine()
                    if (nextLine == null) break
                    body.append(nextLine)
                    body.append("\n")
                }
            } catch (e: IOException) {
                throw e
            } catch (nfe: Resources.NotFoundException) {
                throw nfe
            }
            return body.toString()
        }
    }
}