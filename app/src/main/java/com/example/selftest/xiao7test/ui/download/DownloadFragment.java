package com.example.selftest.xiao7test.ui.download;

import static com.example.selftest.demo6.DownloadConstants.DOWNLOAD_STATE_COMPLETED;
import static com.example.selftest.demo6.DownloadConstants.DOWNLOAD_STATE_PAUSED;
import static com.example.selftest.demo6.DownloadConstants.DOWNLOAD_STATE_RUNNING;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_DOWNLOADING;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_FINISH;
import static com.example.selftest.demo6.DownloadProgressButton.STATE_PAUSE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.selftest.R;
import com.example.selftest.databinding.FragmentDownloadBinding;
import com.example.selftest.demo6.CompletedDownloadEvent;
import com.example.selftest.demo6.DownloadService;
import com.example.selftest.demo6.PauseDownloadEvent;
import com.example.selftest.demo6.UpdateProgressEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * @author LMH
 */
public class DownloadFragment extends Fragment implements View.OnClickListener, DownloadService.DownloadObserver {
    private static final String TAG = "DownloadFragment";
    private ProgressBar progressBar;
    private TextView startDownload;
    private TextView pauseDownload;
    private TextView cancelDownload;
    private TextView tip;
    private boolean isReuseable = false;
    private SharedPreferences sharedPreferences;
    private DownloadService.DownloadBinder downloadBinder;
    private DownloadService downloadService = DownloadService.getInstance();
    private FragmentDownloadBinding binding;
    private boolean isBound = false;
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
    public void onResume() {
        super.onResume();
        initProgressBar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDownloadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        downloadService.registerObserver(this);
        sharedPreferences = getContext().getSharedPreferences("download", Context.MODE_PRIVATE);
        progressBar = root.findViewById(R.id.xiao7_download_pb);
        tip = root.findViewById(R.id.xiao7_tip_tv);
        startDownload = root.findViewById(R.id.xiao7_start_tv);
        pauseDownload = root.findViewById(R.id.xiao7_pause_tv);
        cancelDownload = root.findViewById(R.id.xiao7_cancel_tv);
        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        pauseDownload.setClickable(false);
        cancelDownload.setOnClickListener(this);
        Intent intent = new Intent(getActivity(), DownloadService.class);
        intent.putExtra("flag", "FG");
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (DownloadFragment.this.downloadBinder == null) {
//                }
//                if (!DownloadFragment.this.downloadBinder.isDownloading()) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tip.setText("当前没有下载任务");
//                            startDownload.setVisibility(View.GONE);
//                            pauseDownload.setVisibility(View.GONE);
//                            cancelDownload.setVisibility(View.GONE);
//                        }
//                    });
//                } else {
//
//                }
//            }
//        }).start();
        return root;
    }

    private void initProgressBar() {
        int state = sharedPreferences.getInt("state", -1);
        float progress = sharedPreferences.getFloat("progress", -1f);
        switch (state) {
            case DOWNLOAD_STATE_PAUSED:
                if (progress != -1f) {
                    progressBar.setProgress(Math.round(progress));
                    tip.setText("下载已暂停");
                    isReuseable = false;
                } else {
                    throw new IllegalStateException("Invalid progress");
                }
                break;
            case DOWNLOAD_STATE_RUNNING:
                if (progress != -1f) {
                    progressBar.setProgress(Math.round(progress));
                    tip.setText("正在下载");
                    isReuseable = false;
                } else {
                    throw new IllegalStateException("Invalid progress");
                }
                break;
            case DOWNLOAD_STATE_COMPLETED:
                progressBar.setProgress(100);
                tip.setText("下载已完成");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xiao7_start_tv:
                if (isReuseable) {
                    downloadBinder.resumeDownload();
                } else {
                    startDownload();
                }
                tip.setText("正在下载");
                pauseDownload.setClickable(true);
                startDownload.setClickable(false);
                break;
            case R.id.xiao7_pause_tv:
                tip.setText("下载已暂停");
                downloadBinder.pauseDownload();
                isReuseable = true;
                pauseDownload.setClickable(false);
                startDownload.setClickable(true);
                break;
            case R.id.xiao7_cancel_tv:
                tip.setText("");
                pauseDownload.setClickable(false);
                startDownload.setClickable(true);
                progressBar.setProgress(0);
                downloadBinder.cancelDownload();
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                edit.commit();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateProgressEvent event) {
        Float progress = event.getProgress();
        progressBar.setProgress(Math.round(progress));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompletedEvent(CompletedDownloadEvent event) {
        progressBar.setProgress(100);
        tip.setText("下载已完成");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPauseEvent(PauseDownloadEvent event) {
        Float progress = event.getProgress();
        progressBar.setProgress(Math.round(progress));
    }


    private void startDownload() {
        String url = "https://dl1.msshuo.cn/market/apk/X7Market-4.107.999_1036.4537-prod-official-release.apk";
        String fileName = "LMH.apk";
        if (isBound) {
            downloadBinder.startDownload(url, fileName);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (isBound) {
            getActivity().unbindService(connection);
            isBound = false;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        downloadService.unregisterObserver(this);
    }

    @Override
    public void onDownloadStateChanged() {

    }

    @Override
    public void onDownloadProgressChanged() {

    }
}