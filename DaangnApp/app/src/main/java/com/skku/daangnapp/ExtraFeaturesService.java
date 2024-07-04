package com.skku.daangnapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.androidbrowserhelper.trusted.DelegationService;
import com.google.androidbrowserhelper.locationdelegation.LocationDelegationExtraCommandHandler;

public class ExtraFeaturesService extends DelegationService {

    private static final String CHANNEL_ID = "ExtraFeaturesServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    public ExtraFeaturesService() {
        Log.d("ExtraFeaturesService", "Initializing ExtraFeaturesService");
        registerExtraCommandHandler(new LocationDelegationExtraCommandHandler());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ExtraFeaturesService", "Service Created");
        createNotificationChannel();
        startForegroundService();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ExtraFeaturesService Channel";
            String description = "Channel for ExtraFeaturesService";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("ForegroundServiceType")
    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                        ? PendingIntent.FLAG_IMMUTABLE
                        : 0
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Extra Features Service")
                .setContentText("Service is running")
                .setSmallIcon(R.drawable.ic_launcher_background) // 실제 아이콘 리소스를 사용하세요
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }
}
