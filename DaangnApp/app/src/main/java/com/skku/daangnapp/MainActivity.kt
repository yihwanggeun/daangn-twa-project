package com.skku.daangnapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.ConsoleMessage
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.util.Log
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient

class MainActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        myWebView = findViewById<WebView>(R.id.webView)
        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        WebView.setWebContentsDebuggingEnabled(true)
        myWebView.webViewClient = WebViewClient()
        myWebView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                } else {
                    callback.invoke(origin, true, false)
                }
            }
        }
        myWebView.loadUrl("file:///android_asset/index.html")

        myWebView.addJavascriptInterface(WebAppInterface(this), "Android")

        checkLocPermission()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkLocPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // 권한이 이미 부여됨
            Toast.makeText(this, "Location permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 권한이 부여됨
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    // 권한이 거부됨
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    class WebAppInterface(private val context: MainActivity){
        @android.webkit.JavascriptInterface
        fun showToast(message: String){
            val endTime = System.currentTimeMillis()
            Log.i("Toast Execution Time : ", endTime.toString())
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBackPressed() {
        if(myWebView.canGoBack()){
            myWebView.goBack()
        }
        else{
            super.onBackPressed()
        }
    }
}