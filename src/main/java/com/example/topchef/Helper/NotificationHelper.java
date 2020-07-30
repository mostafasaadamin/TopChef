package com.example.topchef.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.topchef.R;

/**
 * Created by unknown on 7/28/2018.
 */

public class NotificationHelper extends ContextWrapper {
private static final String Top_CHEF_CHANNEL_ID="com.example.unknown.topchef";
    private static final String Top_CHEF_CHANNEL_Name="TOP CHEF";
private NotificationManager manager;
    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel top_channel=new NotificationChannel(Top_CHEF_CHANNEL_ID,Top_CHEF_CHANNEL_Name,NotificationManager.IMPORTANCE_DEFAULT);
        top_channel.enableLights(false);
        top_channel.enableVibration(true);
        top_channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
       getManager().createNotificationChannel(top_channel);
    }

    public NotificationManager getManager() {
        if(manager==null)
        {
            manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;

    }
    public NotificationCompat.Builder settopchefChannel(String title, String body, PendingIntent P_intent, Uri sound)
    {
            return new NotificationCompat.Builder(getApplicationContext(),Top_CHEF_CHANNEL_ID)
                    .setContentIntent(P_intent).setContentTitle(title).setContentText(body)
                    .setSmallIcon(R.drawable.alarm).setSound(sound);

    }
}
