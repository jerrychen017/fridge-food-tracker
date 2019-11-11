package com.oosegroup.fridgefoodtracker.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.oosegroup.fridgefoodtracker.Activities.MainActivity;
import com.oosegroup.fridgefoodtracker.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationController {

    Notification notification;
    NotificationManagerCompat manager;
    Context mContext;
    private static final String CHANNEL_ID = "channel1";
    private static final String textTitle = "Title";
    private static final String textContent = "Description";

    public NotificationController(Context mContext) {
        this.mContext = mContext;
        manager = NotificationManagerCompat.from(mContext);
        createNotificationChannels();
        buildNotification(mContext);
    }

    private void buildNotification(Context mContext) {
        Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_photo_camera_24px)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .build();
       this.notification = notification;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID,
                    "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is Channel 1");
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = mContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    public Notification getNotification() {
        return this.notification;
    }

    public NotificationManagerCompat getManager() {
        return this.manager;
    }


}

