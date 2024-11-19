package com.example.selftest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";
    private DownloadBinder downloadBinder = new DownloadBinder();

    public MyService() {
    }

    public void test() {
        Log.d(TAG, "test: ABCDEFG");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy!");
    }

    class DownloadBinder extends Binder {
        public Service getService() {
            return MyService.this;
        }

        public void startDownload() {
            Log.d(TAG, "startDownload!!!!!!!!!");
        }

        public void getProgress() {
            Log.d(TAG, "getProgress:!!!!!!!!!!");
        }
    }
}