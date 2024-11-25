package com.example.selftest.demo4b;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.selftest.R;

public class Demo4bActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Demo4bActivity";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo4b);
        CustomView customView = findViewById(R.id.custom_view);
        Button button = customView.getButton();
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 处理子按钮的点击事件
        Log.d(TAG, "子按钮被点击了！");
        Toast.makeText(this,"子按钮被点击了!",Toast.LENGTH_SHORT).show();
    }
}