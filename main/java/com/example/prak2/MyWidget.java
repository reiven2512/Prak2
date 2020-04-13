package com.example.prak2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

import java.util.Arrays;

import static android.content.Context.ALARM_SERVICE;
import static java.sql.Types.NULL;

public class MyWidget extends AppWidgetProvider {
    final static String LOG_TAG = "myLogs";
    long alarm;
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
        for(int i : appWidgetIds){
            update(context, appWidgetManager, i);
        }
    }

    public static void update(Context context, AppWidgetManager appWidgetManager,
                         int Id){
        Intent intent = new Intent(context, ConfigActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Id);
        PendingIntent pIntent = PendingIntent.getActivity(context, Id, intent, 0);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
        rv.setOnClickPendingIntent(R.id.tv, pIntent);

        appWidgetManager.updateAppWidget(Id, rv);
    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

}
