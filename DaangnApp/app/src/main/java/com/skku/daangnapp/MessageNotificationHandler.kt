package com.skku.daangnapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object MessageNotificationHandler {

    private const val CHANNEL_ID = "channel_id"

    @SuppressLint("MissingPermission")
    fun showNotificationWithMessage(context: Context, message: String) {
        val intent = Intent().apply {
            action = PostMessageBroadcastReceiver.POST_MESSAGE_ACTION
        }

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Received a message")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_launcher_background, "Reply back", pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(1, builder.build())
    }

}