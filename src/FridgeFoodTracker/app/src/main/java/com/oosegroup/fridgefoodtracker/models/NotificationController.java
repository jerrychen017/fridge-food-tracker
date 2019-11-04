package com.oosegroup.fridgefoodtracker.models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.oosegroup.fridgefoodtracker.Activities.MainActivity;

import androidx.core.app.NotificationCompat;

public class NotificationController {
    private static final String CHANNEL_ID = "channel1";
    Context mContext;
    private static final String textTitle = "Title";
    private static final String textContent = "Description";

    public NotificationController(Context mContext) {
        this.mContext = mContext;
        setupNotificationBuilder(mContext);
        createNotificationChannels();
    }

    private void setupNotificationBuilder(Context mContext) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID,
                    "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is Channel 1");
            NotificationManager manager = mContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
