package com.retroapp.airhockey.airhockey.util

import android.content.Context
import android.content.res.Resources
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class TextResourceReader {
    companion object {
        const val TAG = "TextResourceReader"
        fun readTextFileFromResource(context: Context, resourceId: Int): String{
            val body = StringBuilder()

            try{
                val inputStream = context.resources.openRawResource(resourceId)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)

                var nextLine: String? = null
                while (true){
                    nextLine = bufferedReader.readLine()
                    if(nextLine==null)break
                    body.append(nextLine)
                    body.append("\n")
                }
            }catch (e: IOException){
                throw e
            }catch (nfe: Resources.NotFoundException){
                throw nfe
            }
            return body.toString()
        }
    }
}