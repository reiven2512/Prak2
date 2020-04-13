package com.example.prak2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.Context.ALARM_SERVICE;
import static com.example.prak2.MyWidget.LOG_TAG;

public class AnotherBoadcast extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){
        Log.d(LOG_TAG, "onReceived");
        Bundle extras = intent.getExtras();
        long alarm = extras.getLong(AppWidgetManager.EXTRA_CUSTOM_INFO);
        int id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        String text;
        ComponentName cn = new ComponentName(context, MyWidget.class);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(cn);
        for(int i : ids)
        {
            if(i == id){
                if(alarm > System.currentTimeMillis()){
                    long res = (alarm - System.currentTimeMillis()) / (60000*24*60) ;
                    Log.d(LOG_TAG, Long.toString(res));
                    if(res == 0){
                        res = (alarm - System.currentTimeMillis()) / (60000*60);
                        if(res == 1) text = Long.toString(res) + " Hour";
                        else if(res == 0) text = "Less than an hour";
                        else text = Long.toString(res) + " Hours";
                    }
                    else{
                        if(res == 1) text = Long.toString(res) + " Day";
                        else text = Long.toString(res) + " Days";
                    }
                    Log.d(LOG_TAG, text);
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    Intent intent_2 = new Intent(context, AnotherBoadcast.class);
                    intent_2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent_2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
                    intent_2.putExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, alarm);
                    PendingIntent pIntent = PendingIntent.getBroadcast(context, -1*id, intent_2, 0);
                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60 * 60000, pIntent);

                    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
                    rv.setTextViewText(R.id.tv, text);
                    AppWidgetManager current = AppWidgetManager.getInstance(context);
                    current.updateAppWidget(id, rv);
                }
                break;
            }
        }

    }
}
