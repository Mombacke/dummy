package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import timber.log.Timber
import java.io.OutputStream
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomExceptionHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val defaultUEH: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, e: Throwable) {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "crash_log_$timeStamp.csv"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
            }

            val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            uri?.let {
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    writeCrashLogToStream(outputStream, e, timeStamp)
                }
            }

            Timber.e(e, "Uncaught Exception written to CSV file in Documents directory")
        } catch (fileException: Exception) {
            Timber.e(fileException, "Error while writing exception to CSV file")
        }

        defaultUEH.uncaughtException(thread, e)
    }

    private fun writeCrashLogToStream(outputStream: OutputStream?, e: Throwable, timeStamp: String) {
        outputStream?.let { os ->
            PrintWriter(os).use { writer ->
                writer.println("Timestamp,Exception Type,Message,StackTrace")
                writer.print(timeStamp)
                writer.print(',')
                writer.print(e.javaClass.simpleName)
                writer.print(',')
                writer.print(e.message)
                writer.print(',')
                e.stackTrace.joinToString(separator = " | ") { it.toString() }.let { writer.print(it) }
            }
        }
    }
}



