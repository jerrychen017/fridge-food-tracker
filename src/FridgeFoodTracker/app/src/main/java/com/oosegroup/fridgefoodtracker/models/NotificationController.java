package com.oosegroup.fridgefoodtracker.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.oosegroup.fridgefoodtracker.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * class for handling notifications
 */
public class NotificationController {

    List<Notification> notifications;
    NotificationManagerCompat manager;
    Context mContext;
    String EXPIRING_SOON = "Items expiring soon";
    String EXPIRED = "Items expired";
    private static final String CHANNEL_ID = "channel1";
    private static final String textTitle = "Title";
    private static final String textContent = "Description";


    /**
     * constructor for handlign notifications
     * @param mContext the activity
     * @param fridge the fridge for notifications
     */
    public NotificationController(Context mContext, Fridge fridge) {
        this.mContext = mContext;
        manager = NotificationManagerCompat.from(mContext);
        createNotificationChannels();
        this.notifications = new ArrayList<Notification>();
        buildNotifications(mContext, fridge);
    }

    /**
     * build the notifications
     * @param mContext the activity
     * @param fridge the fridge for the notifications
     */
    private void buildNotifications(Context mContext, Fridge fridge) {
        System.out.println("building notifications");
        Date currentTime = new Date();
        //2 days in milliseconds
        long notificationTime = 172800 * 1000;
        if(fridge.getContent().getItems().isEmpty()) {
            System.out.println("empty");
        }
        for (Item item : fridge.getContent().getItems()) {
            System.out.println("in for loop");
            long diff = item.getDateExpired().getTime() - currentTime.getTime();
            if(diff > 0 && diff <= notificationTime) {
                Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                        .setContentTitle(item.getDescription())
                        .setContentText("Item is expiring soon! Use before " + item.getDateExpired())
                        .setGroup(EXPIRING_SOON)
                        .setSmallIcon(R.drawable.ic_local_grocery_store_24px)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .build();
                this.notifications.add(notification);
            } else if (diff <= 0) {
                Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                        .setContentTitle(item.getDescription())
                        .setContentText("Item is expired!")
                        .setGroup(EXPIRED)
                        .setSmallIcon(R.drawable.ic_local_grocery_store_24px)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .build();
                this.notifications.add(notification);
            }
        }
    }

    /**
     * creates channels for notifications
     */
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

    /**
     * creates list of notifications
     * @return the list of notifications
     */
    public List<Notification> getNotifications() {
        return this.notifications;
    }

    /**
     * manages the notifications
     * @return the manager
     */
    public NotificationManagerCompat getManager() {
        return this.manager;
    }


}

