package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class FileLoggingTree(private val context: Context) : Timber.DebugTree() {
    private val logFileName = "logs.csv"

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        try {
            val logTimeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())

            // Check if log file exists
            val selection = MediaStore.MediaColumns.DISPLAY_NAME + "=?"
            val selectionArgs = arrayOf(logFileName)
            val filesUri = MediaStore.Files.getContentUri("external")
            val cursor = context.contentResolver.query(filesUri, null, selection, selectionArgs, null)

            val isNewFile = cursor?.count == 0
            cursor?.close()

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, logFileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                if (isNewFile) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/YourAppNameLogs")
                }
            }

            val uri = if (isNewFile) {
                context.contentResolver.insert(filesUri, contentValues)
            } else {
                context.contentResolver.query(filesUri, arrayOf(MediaStore.MediaColumns._ID), selection, selectionArgs, null)?.use {
                    if (it.moveToFirst()) {
                        val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                        filesUri.buildUpon().appendPath(id.toString()).build()
                    } else null
                }
            }

            context.contentResolver.openOutputStream(uri ?: return, if (isNewFile) "w" else "wa").use { outputStream ->
                outputStream?.apply {
                    if (isNewFile) {
                        write("Timestamp,Message\n".toByteArray())
                    }
                    write("$logTimeStamp,$message\n".toByteArray())
                    flush()
                    close()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error while logging into file")
        }
    }
}




