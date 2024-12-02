package com.example.selftest.demo6;

import static com.example.selftest.demo6.DownloadConstants.DOWNLOAD_STATE_COMPLETED;
import static com.example.selftest.demo6.DownloadConstants.DOWNLOAD_STATE_PAUSED;
import static com.example.selftest.demo6.DownloadConstants.DOWNLOAD_STATE_RUNNING;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.selftest.R;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LMH
 */
public class DownloadService extends Service {
    private volatile static DownloadService instance;
    private SharedPreferences.Editor editor;
    private static final String TAG = "DownloadService";
    private static final String CHANNEL_ID = "download_channel_id";
    private static final int NOTIFICATION_ID = 1;
    private static final int UPDATE_PROGRESS = 1;
    private static final int PAUSE_PROGRESS = 2;
    private static final int DOWNLOAD_COMPLETE = 3;
    private static final int DOWNLOAD_CANCEL = 4;
    private NotificationManager manager;
    private DownloadBinder mBinder = new DownloadBinder();
    private BaseDownloadTask curDownloadTask;
    private int taskId;
    /**
     * 观察者集合
     */
    private ArrayList<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();

    public DownloadService() {
    }

    public static DownloadService getInstance() {
        if (instance == null) {
            synchronized (DownloadService.class) {
                if (instance == null) {
                    instance = new DownloadService();
                }
            }
        }
        return instance;
    }

    /**
     * 注册观察者
     *
     * @param observer
     */
    public void registerObserver(DownloadObserver observer) {
        if (observer != null && mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    /**
     * 注销观察者
     *
     * @param observer
     */
    public void unregisterObserver(DownloadObserver observer) {
        if (observer != null && mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化SharedPrederences
        SharedPreferences sharedPreferences = getSharedPreferences("download", MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
        Log.d(TAG, "onBind!!!");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind!!!");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy!!!");
        super.onDestroy();
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

        public void pauseDownload() {
            if (curDownloadTask != null) {
                FileDownloader.getImpl().pause(taskId);
            }
        }

        public void resumeDownload() {
            if (curDownloadTask != null) {
                curDownloadTask.reuse();
                curDownloadTask.start();
            }
        }

        public void cancelDownload() {
            if (curDownloadTask!= null) {
                FileDownloader.getImpl().clearAllTaskData();
                updateNotification("下载取消",0,DOWNLOAD_CANCEL);
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
                .setForceReDownload(true)
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
                        Float progress = (Float) ((soFarBytes * 1.0f / totalBytes) * 100);
                        updateSharedPreferences(DOWNLOAD_STATE_RUNNING, progress);
                        EventBus.getDefault().post(new UpdateProgressEvent(progress));
                        Log.d(TAG, "progress: " + progress + "%");
                        updateNotification("下载中", progress, UPDATE_PROGRESS);
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
                        updateSharedPreferences(DOWNLOAD_STATE_COMPLETED, -1);
                        updateNotification("下载完成", 100, DOWNLOAD_COMPLETE);
                        EventBus.getDefault().post(new CompletedDownloadEvent());
                        stopForeground(Service.STOP_FOREGROUND_REMOVE);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "paused: " + soFarBytes + " / " + totalBytes);
                        Float progress = (Float) ((soFarBytes * 1.0f / totalBytes) * 100);
                        EventBus.getDefault().post(new PauseDownloadEvent(progress));
                        updateSharedPreferences(DOWNLOAD_STATE_PAUSED, progress);
                        updateNotification("下载暂停", progress, PAUSE_PROGRESS);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    protected void error(BaseDownloadTask task, Throwable ex) {
                        Log.e(TAG, "error: ", ex);
                        stopForeground(Service.STOP_FOREGROUND_REMOVE);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                });
        curDownloadTask = downloadTask;
        taskId = downloadTask.start();
    }

    private void updateSharedPreferences(int state, float progress) {
        editor.putInt("state", state);
        editor.putFloat("progress", progress);
        editor.commit();
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

    private void updateNotification(String title, float progress, int flag) {
        int icon = android.R.drawable.stat_sys_download;
        String content = progress == 100 ? "下载完成" : "正在下载...";
        switch (flag) {
            case PAUSE_PROGRESS:
                icon = R.mipmap.ic_pause;
                content = "下载已暂停";
                break;
            case DOWNLOAD_COMPLETE:
                icon = android.R.drawable.stat_sys_download_done;
                content = "下载已完成";
                break;
            case DOWNLOAD_CANCEL:
                icon = android.R.drawable.stat_sys_warning;
                content = "下载已取消";
            default:
                break;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.stat_sys_download))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100, (int) progress, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * 定义观察者应该实现的方法
     */
    public interface DownloadObserver {
        public void onDownloadStateChanged();

        public void onDownloadProgressChanged();
    }

    // 5.通知下载状态发生变化
    public synchronized void notifyDownloadStateChanged() {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadStateChanged();
        }
    }

    // 6.通知下载进度发生变化
    public synchronized void notifyDownloadProgressChanged() {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadProgressChanged();
        }
    }
}