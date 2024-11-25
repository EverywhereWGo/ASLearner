package com.example.selftest.downloadbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.selftest.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by 犀利的小牛 on 2016/8/8.
 */
public class DownloadButtonActivity extends AppCompatActivity implements DownLoadButton.OnDownLoadButtonClickListener {

    private DownLoadButton downLoadButton;
    private int downLoadedPrecent = 0;
    private boolean isPaused = false;
    private Handler handler;
    private boolean shouldPauseProgressUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_button);

        // 注册EventBus
        EventBus.getDefault().register(this);

        downLoadButton = (DownLoadButton) findViewById(R.id.downLoadButton);
        downLoadButton.setOnDownLoadButtonClickListener(this);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销EventBus，避免内存泄漏
        EventBus.getDefault().unregister(this);
        // 移除所有未执行的Handler任务，防止内存泄漏
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v, int curState, int curPrecent) {
        if (curState == DownLoadButton.STATE_NORMAL) {
            //开始下载
            downLoadButton.setState(DownLoadButton.STATE_DOWNLOADING);
            // 发布开始下载的事件
            EventBus.getDefault().post(new DownloadStartEvent());
        } else if (curState == DownLoadButton.STATE_DOWNLOADING) {
            //下载中如果被点击时停止下载，这里可以根据自个的需求换成暂停或者其他
            if (isPaused) {
                // 如果当前是暂停状态，再次点击则继续下载
                isPaused = false;
                downLoadButton.setState(DownLoadButton.STATE_DOWNLOADING);
                // 继续下载，重新触发进度更新逻辑
                continueDownload();
            } else {
                // 如果当前不是暂停状态，点击则暂停下载
                isPaused = true;
                downLoadButton.setState(DownLoadButton.STATE_NORMAL);
                downLoadedPrecent = curPrecent;
                shouldPauseProgressUpdate = true;
                // 发布停止下载的事件
                EventBus.getDefault().post(new DownloadStopEvent());
            }
        } else {
            downLoadButton.setState(DownLoadButton.STATE_NORMAL);
            downLoadedPrecent = 0;
            downLoadButton.setDownLoadProgress(0);
            handler.removeCallbacksAndMessages(null);
            isPaused = false;
        }
    }

    // 订阅开始下载事件的处理方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadStart(DownloadStartEvent event) {
        if (shouldPauseProgressUpdate) {
            // 如果标志位为true，表示需要暂停进度更新，直接返回
            shouldPauseProgressUpdate = false;
            return;
        }
        // 模拟下载进度增加
        downLoadedPrecent += 10;
        if (downLoadedPrecent >= 100) {
            downLoadButton.setState(DownLoadButton.STATE_COMPLETE);
        } else {
            downLoadButton.setDownLoadProgress(downLoadedPrecent);
            // 发布下载进度更新事件
            EventBus.getDefault().post(new DownloadProgressEvent(downLoadedPrecent));
            // 循环增加进度
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onDownloadStart(event);
                }
            }, 400);
        }
    }

    // 订阅停止下载事件的处理方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadStop(DownloadStopEvent event) {
        handler.removeCallbacksAndMessages(null);
    }

    // 订阅下载进度更新事件的处理方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadProgress(DownloadProgressEvent event) {
        downLoadButton.setDownLoadProgress(event.getProgress());
    }

    // 定义开始下载事件类
    public static class DownloadStartEvent {
    }


    /**
     * 定义停止下载事件类
     */
    public static class DownloadStopEvent {
    }

    // 定义下载进度更新事件类
    public static class DownloadProgressEvent {
        private int progress;

        public DownloadProgressEvent(int progress) {
            this.progress = progress;
        }

        public int getProgress() {
            return progress;
        }
    }

    private void continueDownload() {
        downLoadButton.setDownLoadProgress(downLoadedPrecent);
        // 重置暂停进度更新标志位
        shouldPauseProgressUpdate = false;
        EventBus.getDefault().post(new DownloadStartEvent());
    }
}