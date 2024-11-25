package com.example.selftest.demo3d;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.selftest.R;

public class Demo3dActivity extends AppCompatActivity {
    private static final String TAG = "Demo3dActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3d);
        ProgressButton progressButton = findViewById(R.id.button_progress_green);
        progressButton.setMinProgress(0);
        progressButton.setMaxProgress(100);
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressButton.setProgress(finalI);
                                    Log.d(TAG, "run: " + finalI);
                                }
                            });
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }
}