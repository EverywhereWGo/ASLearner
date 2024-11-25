package com.example.selftest.demo4a;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.example.selftest.R;

public class Demo4aActivity extends AppCompatActivity {
    private static final String TAG = "Demo4aActivity";
    ImageView draggableImageView;
    Integer screenWidth;
    Integer screenHeight;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo4a);
        draggableImageView = findViewById(R.id.draggable_image_view);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels-110;
        Log.d(TAG, "screenWidth->"+screenWidth);
        Log.d(TAG, "screenHeight->"+screenHeight);
        draggableImageView.setOnTouchListener(new View.OnTouchListener() {
            private float dX,dY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX()-event.getRawX();
                        dY = v.getY()-event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float newX = Math.max(0,Math.min(event.getRawX()+dX,screenWidth-v.getWidth()));
                        float newY = Math.max(0,Math.min(event.getRawY()+dY,screenHeight-v.getHeight()));
                        v.animate()
                                .x(newX)
                                .y(newY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}