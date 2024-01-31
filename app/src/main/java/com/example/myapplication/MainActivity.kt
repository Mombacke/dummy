package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("MainActivity onCreate")

        findViewById<Button>(R.id.button).setOnClickListener {
            Timber.i("Log button clicked")

            simulateDatabaseOperation()
        }
    }

    private fun simulateDatabaseOperation() {
            Timber.i("Simulating database operation")

            Timber.i("Database operation successful")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("MainActivity onResume")
    }
}





