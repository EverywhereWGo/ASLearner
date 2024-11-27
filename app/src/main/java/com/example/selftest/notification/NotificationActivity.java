package com.example.selftest.notification;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.selftest.R;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNoticeButton = findViewById(R.id.send_notice);
        sendNoticeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notice:
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //高版本需要渠道
                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    //只在Android O之上需要渠道
                    NotificationChannel notificationChannel = new NotificationChannel("channelid1", "channelname", NotificationManager.IMPORTANCE_HIGH);
                    //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
                    manager.createNotificationChannel(notificationChannel);
                }
                Intent intent = new Intent(this, NotifActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                Notification notification = new NotificationCompat.Builder(this, "channelid1")
                        .setContentTitle("NOTICE")
                        .setContentText("ABCDFRTT")
                        .setSmallIcon(R.drawable.grape)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(Boolean.TRUE)
                        .setPriority(5)
                        .build();;
                manager.notify(1, notification);
                break;
            default:
                break;
        }
    }

}