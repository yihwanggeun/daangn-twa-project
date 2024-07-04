package com.skku.daangnapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.*
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.androidbrowserhelper.demos.twapostmessage.MessageNotificationHandler
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class MainActivity : AppCompatActivity() {

    private var mClient: CustomTabsClient? = null
    private var mSession: CustomTabsSession? = null
    private val URL = Uri.parse("https://192.168.0.2:3000")
    private val SOURCE_ORIGIN = Uri.parse("https://192.168.0.2:3000")
    private val TARGET_ORIGIN = Uri.parse("https://192.168.0.2:3000")
    private var mValidated = false
    private val TAG = "PostMessageDemo"
    private val gson = Gson()

    private val requestLocationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Location permission granted, binding Custom Tabs service")
            bindCustomTabsService()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Activity created")
        setContentView(R.layout.activity_main)

        val serviceIntent = Intent(this, ExtraFeaturesService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        Log.d("MainActivity", "ExtraFeaturesService started")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: Requesting location permission")
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            Log.d(TAG, "onCreate: Location permission already granted, binding Custom Tabs service")
            bindCustomTabsService()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private val customTabsCallback = object : CustomTabsCallback() {
        override fun onPostMessage(message: String, extras: Bundle?) {
            super.onPostMessage(message, extras)
            Log.d(TAG, "Got message: $message")

            if (message.contains("ACK")) {
                Log.d(TAG, "onPostMessage: Message contains ACK, returning")
                return
            }

            try {
                val jsonData = gson.fromJson(message, Map::class.java)
                Log.d(TAG, "onPostMessage: Received JSON data: $jsonData")

                if (jsonData["message"] == "DetailPage") {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Json Data Receive", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "JsonSyntaxException: ${e.message}")
            }

            MessageNotificationHandler.showNotificationWithMessage(this@MainActivity, message)
            Log.d(TAG, "onPostMessage: Showing notification with message: $message")
        }

        override fun onRelationshipValidationResult(relation: Int, requestedOrigin: Uri, result: Boolean, extras: Bundle?) {
            Log.d(TAG, "onRelationshipValidationResult: relation=$relation, requestedOrigin=$requestedOrigin, result=$result, extras=$extras")

            if (!result) {
                Log.d(TAG, "Relationship validation failed for origin: $requestedOrigin with relation: $relation")
                if (extras != null) {
                    for (key in extras.keySet()) {
                        Log.d(TAG, "Extra: $key = ${extras.get(key)}")
                    }
                }
            } else {
                Log.d(TAG, "Relationship validation succeeded for origin: $requestedOrigin with relation: $relation")
            }

            mValidated = result
        }

        override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
            Log.d(TAG, "onNavigationEvent: navigationEvent=$navigationEvent, extras=$extras")
            if (navigationEvent != NAVIGATION_FINISHED) {
                Log.d(TAG, "onNavigationEvent: Navigation not finished, returning")
                return
            }

            if (!mValidated) {
                Log.d(TAG, "Not starting PostMessage as validation didn't succeed.")
            }

            val result = mSession?.requestPostMessageChannel(SOURCE_ORIGIN, TARGET_ORIGIN, Bundle()) ?: false
            Log.d(TAG, "Requested Post Message Channel: $result")
        }

        override fun onMessageChannelReady(extras: Bundle?) {
            Log.d(TAG, "Message channel ready.")

            val result = mSession?.postMessage("First message from Android", null) ?: 0
            Log.d(TAG, "postMessage returned: $result")
        }
    }

    private fun bindCustomTabsService() {
        Log.d(TAG, "bindCustomTabsService: Attempting to bind Custom Tabs service")
        val packageName = CustomTabsClient.getPackageName(this, null)
        if (packageName == null) {
            Log.d(TAG, "bindCustomTabsService: Custom Tabs service package not found")
            Toast.makeText(this, "Custom Tabs 서비스 패키지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Binding to $packageName", Toast.LENGTH_SHORT).show()

        val success = CustomTabsClient.bindCustomTabsService(this, packageName, object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
                Log.d(TAG, "onCustomTabsServiceConnected: Custom Tabs service connected")
                mClient = client
                client.warmup(0L)
                mSession = mClient!!.newSession(customTabsCallback)
                Log.d(TAG, "New session created")
                launch()
                registerBroadcastReceiver()
            }

            override fun onServiceDisconnected(name: ComponentName) {
                Log.d(TAG, "onServiceDisconnected: Custom Tabs service disconnected")
                mClient = null
            }
        })

        if (!success) {
            Log.e(TAG, "Failed to bind Custom Tabs service")
        }
    }

    private fun launch() {
        Log.d(TAG, "Launching Trusted Web Activity with URL: $URL")
        mSession?.let {
            TrustedWebActivityIntentBuilder(URL).build(it).launchTrustedWebActivity(this)
        }
    }

    @SuppressLint("WrongConstant")
    private fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(PostMessageBroadcastReceiver.POST_MESSAGE_ACTION)
        ContextCompat.registerReceiver(
            this, mSession?.let { PostMessageBroadcastReceiver(it) },
            intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }
}
