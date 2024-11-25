package com.example.selftest.demo4b;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * @author LMH
 */
public class CustomView extends FrameLayout {
    private static final String TAG = "CustomView";

    private float mLastX;
    private float mLastY;
    private boolean isDragging = false;

    public Button getButton() {
        return button;
    }

    private Button button;
    Integer screenWidth;
    Integer screenHeight;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 110;
        // 创建一个子按钮
        button = new Button(getContext());
        button.setText("点击我");
        // 设置子按钮的宽度和高度为固定100dp
        Context context = getContext();
        int buttonWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                100,
                context.getResources().getDisplayMetrics()
        );
        int buttonHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                100,
                context.getResources().getDisplayMetrics()
        );
        LayoutParams buttonParams = new LayoutParams(buttonWidth, buttonHeight);
        button.setLayoutParams(buttonParams);
        addView(button);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "avtion->" + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getRawX() - this.getX();
                mLastY = ev.getRawY() - this.getY();
                isDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isDragging = true;
                break;
            default:
                break;
        }
        // 如果是拖动操作，则拦截触摸事件，由本View处理后续的触摸动作
        return isDragging;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                isDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "mLastX->" + mLastX);
                float newX = Math.max(0, Math.min(event.getRawX() - mLastX, screenWidth - this.getWidth()));
                float newY = Math.max(0, Math.min(event.getRawY() - mLastY, screenHeight - this.getHeight()));
                this.animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start();
                break;
            default:
                break;
        }
        return true;
    }

}