package com.skku.daangnapp

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.trusted.TrustedWebActivityIntentBuilder

class MainActivity : AppCompatActivity() {
    private val twaUrl = "https://192.168.0.2:3000"
    private var customTabsSession: CustomTabsSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bind to the Custom Tabs service and launch the TWA
        bindCustomTabsService()
    }

    private fun bindCustomTabsService() {
        val serviceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
                client.warmup(0L) // Warm up the browser
                customTabsSession = client.newSession(object : CustomTabsCallback() {
                    override fun onPostMessage(message: String, extras: Bundle?) {
                        super.onPostMessage(message, extras)
                        message?.let {
                            runOnUiThread {
                                Log.i("message", "Message Received: $it")
                                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }) // Create a new session
                launchTWA()
            }

            override fun onServiceDisconnected(name: ComponentName) {
                customTabsSession = null
            }
        }
        CustomTabsClient.bindCustomTabsService(this, "com.android.chrome", serviceConnection)
    }

    private fun launchTWA() {
        try {
            val uri = Uri.parse(twaUrl)
            val twaBuilder = TrustedWebActivityIntentBuilder(uri)
            val twaIntent = twaBuilder.build(customTabsSession!!)

            val customTabsIntent = CustomTabsIntent.Builder(customTabsSession)
                .build()

            customTabsIntent.intent.setPackage("com.android.chrome")
            customTabsIntent.intent.putExtras(twaIntent.intent.extras!!)
            startActivity(customTabsIntent.intent)
            finish() // Close the MainActivity once TWA is launched
        } catch (e: Exception) {
            e.printStackTrace()
            // If Chrome is not installed, launch the URL in the default browser
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(twaUrl))
            startActivity(browserIntent)
            Toast.makeText(this, "Chrome not found, using default browser", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
