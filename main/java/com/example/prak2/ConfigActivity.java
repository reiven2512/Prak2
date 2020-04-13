package com.example.prak2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.prak2.MyWidget.LOG_TAG;

public class ConfigActivity extends AppCompatActivity {
    Button bt;
    EditText cv;
    NotificationManager nm;
    int widgetID;
    Intent result;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if(widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) finish();
        result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        bt = findViewById(R.id.bt);
        cv = findViewById(R.id.calendar);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "clicked");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String tmp = cv.getText().toString() + " 00:00:00:000";
                Date a = new Date();
                Log.d(LOG_TAG, tmp);

                try {
                    a = sdf.parse(tmp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                AlarmManager am_2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                long temp = 9*60*60000;
                Intent intent = new Intent(ConfigActivity.this, MyBroadcast.class);
                intent.putExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, a.getTime());
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
                PendingIntent pi = PendingIntent.getBroadcast(ConfigActivity.this, widgetID, intent, 0);
                Log.d(LOG_TAG, sdf.format(a.getTime() + temp));
                am.set(AlarmManager.RTC_WAKEUP, a.getTime() + temp, pi);

                Intent intent_2 = new Intent(ConfigActivity.this, AnotherBoadcast.class);
                intent_2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent_2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
                intent_2.putExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, a.getTime() + temp);
                PendingIntent pIntent = PendingIntent.getBroadcast(ConfigActivity.this, -1*widgetID, intent_2, 0);
                am_2.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pIntent);
                MyWidget.update(ConfigActivity.this, AppWidgetManager.getInstance(ConfigActivity.this), widgetID);
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}
