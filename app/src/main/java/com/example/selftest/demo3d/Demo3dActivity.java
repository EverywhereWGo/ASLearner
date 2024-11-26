package com.example.selftest.demo3d;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.selftest.R;

/**
 * @author LMH
 */
public class Demo3dActivity extends AppCompatActivity {
    private static final String TAG = "Demo3dActivity";
    DownloadProgressButton mDownloadProgressButton;
    Button mReset;
    Button mBorder;
    SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3d);
        mReset = (Button) findViewById(R.id.reset);
        mBorder = (Button) findViewById(R.id.border);
        mDownloadProgressButton = (DownloadProgressButton) findViewById(R.id.download_btn);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mDownloadProgressButton.setShowBorder(false);
        mDownloadProgressButton.setCurrentText("安装");
        mDownloadProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTheButton();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownloadProgressButton.setState(DownloadProgressButton.STATE_NORMAL);
                mDownloadProgressButton.setCurrentText("安装");
                mDownloadProgressButton.setProgress(0);
            }
        });

        mBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownloadProgressButton.setShowBorder(true);
                mDownloadProgressButton.postInvalidate();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDownloadProgressButton.setButtonRadius((progress / 100.0f) * mDownloadProgressButton.getHeight() / 2);
                mDownloadProgressButton.postInvalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void showTheButton() {

        if (mDownloadProgressButton.getProgress() + 10 > 100) {
            mDownloadProgressButton.setState(DownloadProgressButton.STATE_FINISH);
            mDownloadProgressButton.setCurrentText("安装中");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDownloadProgressButton.setState(DownloadProgressButton.STATE_NORMAL);
                    mDownloadProgressButton.setCurrentText("打开");
                }
            }, 2000);

        }

        if (mDownloadProgressButton.getState() == DownloadProgressButton.STATE_NORMAL
                || mDownloadProgressButton.getState() == DownloadProgressButton.STATE_PAUSE) {
            mDownloadProgressButton.setState(DownloadProgressButton.STATE_DOWNLOADING);
            mDownloadProgressButton.setProgressText("下载中", mDownloadProgressButton.getProgress() + 8);
            return;
        }

        if (mDownloadProgressButton.getState() == DownloadProgressButton.STATE_DOWNLOADING) {
            mDownloadProgressButton.setState(DownloadProgressButton.STATE_PAUSE);
            mDownloadProgressButton.setCurrentText("继续");
            return;
        }


    }
}