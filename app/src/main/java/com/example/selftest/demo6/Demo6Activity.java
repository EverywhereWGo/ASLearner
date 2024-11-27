package com.example.selftest.demo6;

import static com.example.selftest.demo6.DownloadProgressButton.STATE_DOWNLOADING;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_FINISH;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_NORMAL;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_PAUSE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.selftest.R;
import com.example.selftest.dialog.DialogActivity;
import com.example.selftest.dialog.InputInfoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author LMH
 */
public class Demo6Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Demo6Activity";
    private DownloadProgressButton downloadProgressButton;
    private DownloadService.DownloadBinder downloadBinder;
    private boolean isBound;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRegisterd = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo6);
        downloadProgressButton = findViewById(R.id.download_bt);
        downloadProgressButton.setShowBorder(false);
        downloadProgressButton.setCurrentText("安装");
        downloadProgressButton.setOnClickListener(this);
        if (!isRegisterd) {
            EventBus.getDefault().register(this);
            isRegisterd = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_bt:
                switch (downloadProgressButton.getState()) {
                    case STATE_NORMAL:
                        startDownload();
                        break;
                    case STATE_DOWNLOADING:
                        downloadBinder.pauseDownload();
                        downloadProgressButton.setState(STATE_PAUSE);
                        break;
                    case STATE_PAUSE:
                        downloadBinder.resumeDownload();
                        downloadProgressButton.setState(STATE_DOWNLOADING);
                        break;
                    case STATE_FINISH:
                        downloadProgressButton.setState(STATE_NORMAL);
                        downloadProgressButton.setCurrentText("安装");
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void startDownload() {
        String url = "https://dl1.msshuo.cn/market/apk/X7Market-4.107.999_1036.4537-prod-official-release.apk";
        String fileName = "LMH.apk";
        Intent intent = new Intent(Demo6Activity.this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        if (isBound) {
            downloadBinder.startDownload(url, fileName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateProgressEvent event) {
        downloadProgressButton.setState(com.example.selftest.demo3d.DownloadProgressButton.STATE_DOWNLOADING);
        downloadProgressButton.setProgressText("下载中", event.getProgress() * 1.0f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompletedEvent(CompletedDownloadEvent event) {
        downloadProgressButton.setState(com.example.selftest.demo3d.DownloadProgressButton.STATE_FINISH);

        downloadProgressButton.setCurrentText("下载完成");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (isRegisterd) {
            EventBus.getDefault().unregister(this);
            isRegisterd = false;
        }
    }

}