package com.wambly.iytem;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // show toast
        final String[] menu = new String[1];
        menu[0] = "No Menu";
        new Thread(new Runnable() {
            @Override
            public void run() {
                menu[0] = "  ";
            }
        }).start();
        sendMenuNotification(context,menu);
        Log.e("RECEvie","send");

    }

    public void sendMenuNotification(final Context context, final String[] menu){
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Menu")
                        .setContentText("Menu of the Day")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(menu[0].substring(0,menu[0].length()-1)))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build());
            }
        }, 2000);
    }
}

