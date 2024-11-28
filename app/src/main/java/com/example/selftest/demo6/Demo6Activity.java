package com.example.selftest.demo6;

import static com.example.selftest.demo6.DownloadProgressButton.STATE_DOWNLOADING;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_FINISH;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_NORMAL;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_PAUSE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
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
public class Demo6Activity extends AppCompatActivity implements View.OnClickListener, ActivityResultCallback<ActivityResult> {
    private static final String TAG = "Demo6Activity";
    private DownloadProgressButton downloadProgressButton;
    private DownloadService.DownloadBinder downloadBinder;
    private boolean isBound;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRegisterd = false;
    private ActivityResultLauncher launcher;
    // 自定义RequestCode
    private static final int WRITE_EXTERNAL_STORAGE = 1;
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
        Intent intent = new Intent(Demo6Activity.this, DownloadService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        downloadProgressButton = findViewById(R.id.download_bt);
        downloadProgressButton.setShowBorder(false);
        downloadProgressButton.setCurrentText("安装");
        downloadProgressButton.setOnClickListener(this);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);
        if (!isRegisterd) {
            EventBus.getDefault().register(this);
            isRegisterd = true;
        }
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        // Android 11 特殊权限处理的回调
        if (result.getResultCode() == RESULT_OK && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                startDownload();
            } else {
                Toast.makeText(Demo6Activity.this, "没有写入外部内存的权限", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "没有权限");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Android 6 危险权限处理的回调
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else {
                Toast.makeText(this, "没有写入外部内存的权限", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "没有权限");
            }
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.download_bt:
                    int state = downloadProgressButton.getState();
                    switch (state) {
                        case STATE_NORMAL:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                // Android 11 特殊权限申请处理
                                if (Environment.isExternalStorageManager()) {
                                    startDownload();
                                } else {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                    intent.setData(Uri.parse("package:" + this.getPackageName()));
                                    launcher.launch(intent);
                                }
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // Android 6 危险权限申请处理
                                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(this
                                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                            , WRITE_EXTERNAL_STORAGE);
                                } else {
                                    // 如果已经授权就可以直接执行
                                    startDownload();
                                }
                            } else {
                                // Android 6以下 无需进行权限处理
                                startDownload();
                            }
                            break;
                        case STATE_DOWNLOADING:
                            downloadBinder.pauseDownload();
                            downloadProgressButton.setState(STATE_PAUSE);
                            downloadProgressButton.setCurrentText("继续");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startDownload() {
        String url = "https://dl1.msshuo.cn/market/apk/X7Market-4.107.999_1036.4537-prod-official-release.apk";
        String fileName = "LMH.apk";
        if (isBound) {
            downloadBinder.startDownload(url, fileName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateProgressEvent event) {
        downloadProgressButton.setState(com.example.selftest.demo3d.DownloadProgressButton.STATE_DOWNLOADING);
        downloadProgressButton.setProgressText(event.getProgress() * 1.0f);
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