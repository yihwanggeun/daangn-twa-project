package com.skku.daangnapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsSession


class PostMessageBroadcastReceiver(private val customTabsSession: CustomTabsSession) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "Got message: ")

        customTabsSession.postMessage("Got it!", null)
    }

    companion object {
        const val POST_MESSAGE_ACTION = "com.example.postmessage.POST_MESSAGE_ACTION"
    }
}