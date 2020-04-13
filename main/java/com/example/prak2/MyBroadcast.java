package com.example.prak2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Notification.PRIORITY_HIGH;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.prak2.MyWidget.LOG_TAG;

public class MyBroadcast extends BroadcastReceiver {
    private static final String CHANNEL_ID = "CHANNEL_ID";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long time = intent.getExtras().getLong(AppWidgetManager.EXTRA_CUSTOM_INFO);
        int id = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Alarm")
                        .setContentText(sdf.format(time))
                        .setPriority(PRIORITY_HIGH);;

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        createChannelIfNeeded(notificationManager);
        notificationManager.notify(1, notification);

        String text = "Click to set alarm";
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
        rv.setTextViewText(R.id.tv, text);
        AppWidgetManager current = AppWidgetManager.getInstance(context);
        current.updateAppWidget(id, rv);
    }
    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    public void backToConfig(Context context){
        Intent intent = new Intent(context, ConfigActivity.class);
        intent.putExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, 's');
    }
}
