package com.autoglm.helper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {

    private lateinit var statusText: TextView
    private lateinit var serverStatusText: TextView
    private lateinit var openSettingsButton: Button
    private lateinit var testConnectionButton: Button
    
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateStatus()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        statusText = findViewById(R.id.statusText)
        serverStatusText = findViewById(R.id.serverStatusText)
        openSettingsButton = findViewById(R.id.openSettingsButton)
        testConnectionButton = findViewById(R.id.testConnectionButton)
        
        openSettingsButton.setOnClickListener {
            openAccessibilitySettings()
        }
        
        testConnectionButton.setOnClickListener {
            testConnection()
        }
        
        updateStatus()
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }

    private fun updateStatus() {
        val service = AutoGLMAccessibilityService.getInstance()
        
        if (service != null) {
            statusText.text = getString(R.string.service_running)
            serverStatusText.text = getString(
                R.string.server_status,
                getString(R.string.server_running, AutoGLMAccessibilityService.PORT)
            )
        } else {
            statusText.text = getString(R.string.service_stopped)
            serverStatusText.text = getString(
                R.string.server_status,
                getString(R.string.server_stopped)
            )
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun testConnection() {
        Thread {
            try {
                val url = URL("http://localhost:${AutoGLMAccessibilityService.PORT}/status")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 3000
                connection.readTimeout = 3000
                
                val responseCode = connection.responseCode
                val response = connection.inputStream.bufferedReader().readText()
                
                runOnUiThread {
                    if (responseCode == 200) {
                        Toast.makeText(
                            this,
                            getString(R.string.connection_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.connection_failed, "HTTP $responseCode"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        getString(R.string.connection_failed, e.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
    }
}
