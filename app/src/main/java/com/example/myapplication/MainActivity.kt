package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        // Set up a click listener to cause a crash
        button.setOnClickListener {
            Timber.d("Button clicked, about to crash")
            throw RuntimeException("Deliberate crash for testing")
        }

        // Example Timber logging
        Timber.d("MainActivity created")
    }

    override fun onStart() {
        super.onStart()
        Timber.d("MainActivity started")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("MainActivity resumed")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("MainActivity paused")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("MainActivity stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("MainActivity destroyed")
    }
}
