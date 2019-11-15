package com.carlospinan.airhockey2.extensions

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

fun Context.readTextFileFromResource(resourceId: Int): String {
    val body = StringBuilder()
    this.apply {
        val stream = resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(stream)
        val bufferedReader = BufferedReader(inputStreamReader as Reader?)
        var newLine: String? = bufferedReader.readLine()
        while (newLine != null) {
            body.append(newLine).append("\n")
            newLine = bufferedReader.readLine()
        }
    }
    return body.toString()
}