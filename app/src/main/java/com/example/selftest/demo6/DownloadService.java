package com.example.selftest.demo6;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * @author LMH
 */
public class DownloadService extends Service{

    private static final String TAG = "DownloadService";
    private static final String CHANNEL_ID = "download_channel_id";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager manager;
    private DownloadBinder mBinder = new DownloadBinder();
    private BaseDownloadTask curDownloadTask;
    private int taskId;
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化FileDownloader
        FileDownloader.setup(getApplicationContext());
        // 创建通知渠道（仅适用于Android O及以上版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "下载通知",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DownloadBinder extends Binder {
        public void startDownload(String url, String fileName) {
            try {
                startForeground(NOTIFICATION_ID, getNotification("开始下载", 0));
                downloadFile(url, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void pauseDownload(){
            if(curDownloadTask != null){
                FileDownloader.getImpl().pause(taskId);
            }
        }
        public void resumeDownload(){
            if(curDownloadTask != null){
                curDownloadTask.reuse();
                curDownloadTask.start();
            }
        }
    }

    private void downloadFile(String url, String fileName) {
//        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                + File.separator + fileName;
        String filePath = Environment.getExternalStorageDirectory().getPath()
                + File.separator + fileName;
        BaseDownloadTask downloadTask = FileDownloader.getImpl().create(url)
                .setPath(filePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "pending: " + soFarBytes + " / " + totalBytes);
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "connected: " + soFarBytes + " / " + totalBytes);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int progress = (int) ((soFarBytes * 1.0f / totalBytes) * 100);
                        EventBus.getDefault().post(new UpdateProgressEvent(progress));
                        Log.d(TAG, "progress: " + progress + "%");
                        updateNotification("下载中", progress);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.d(TAG, "blockComplete");
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        Log.d(TAG, "retry: " + retryingTimes);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d(TAG, "completed");
                        stopForeground(Service.STOP_FOREGROUND_REMOVE);
                        updateNotification("下载完成", 100);
                        EventBus.getDefault().post(new CompletedDownloadEvent());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "paused: " + soFarBytes + " / " + totalBytes);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    protected void error(BaseDownloadTask task, Throwable ex) {
                        Log.e(TAG, "error: ", ex);
                        stopForeground(Service.STOP_FOREGROUND_REMOVE);
                        updateNotification("下载失败", 0);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                });
        curDownloadTask = downloadTask;
        taskId = downloadTask.start();
    }

    private Notification getNotification(String title, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(progress == 100 ? "下载完成" : "正在下载...")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.stat_sys_download))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100, progress, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
        return builder.build();
    }

    private void updateNotification(String title, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(progress == 100 ? "下载完成" : "正在下载...")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.stat_sys_download))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100, progress, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}