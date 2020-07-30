package com.example.topchef.RealTimeNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.topchef.Helper.NotificationHelper;
import com.example.topchef.Login;
import com.example.topchef.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

public class fcmmessageingservice extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String,String >data=remoteMessage.getData();
        String title=data.get("title");
        String message=data.get("message");
        String lat = data.get("lat");
        String lng = data.get("lng");
        String order_id = data.get("Order");
        String shipper=data.get("shipper");
        if (lat != null && lng != null && order_id != null&&shipper!=null)
        {
            Paper.init(this);
            String order=Paper.book().read("order_id","no");
            if(order_id.equals(order)) {
                Log.i("remotey", "lat" + lat + "lng" + lng + "order" + order_id+"shipper"+shipper);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.unknown.topchef");
                broadcastIntent.putExtra("lat", Double.parseDouble(lat));
                broadcastIntent.putExtra("lng", Double.parseDouble(lng));
                broadcastIntent.putExtra("shipper", shipper);
                sendBroadcast(broadcastIntent);
            }
        }
        if (title != null && message != null) {
            Log.i("respon", "onMessageReceived: " + data);
            Intent i = new Intent(getApplicationContext(), Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pen = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder no = new NotificationCompat.Builder(this, "M_CH_ID");
            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/raw/sound");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationHelper helper = new NotificationHelper(this);
                NotificationCompat.Builder builder = helper.settopchefChannel(title, message, pen, sound);
                helper.getManager().notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), builder.build());
            } else {
                no.setSound(sound);
               // no.(R.drawable.alerter_ic_notifications);
                no.setContentTitle(title);
                no.setContentText(message);
                no.setSmallIcon(R.drawable.alerter_ic_notifications);
                no.setAutoCancel(true);
                no.setContentIntent(pen);
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), no.build());
            }
        }
        super.onMessageReceived(remoteMessage);
    }
}
