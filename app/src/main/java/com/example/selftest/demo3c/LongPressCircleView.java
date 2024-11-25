package com.example.selftest.demo3c;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


/**
 * @author LMH
 */
public class LongPressCircleView extends View {

    private Paint circlePaint;
    private GestureDetector gestureDetector;
    private Vibrator vibrator;
    private boolean isLongPressTriggered = false;
    private Circle longPressCircle;
    private Context context;
    private Long pressStartTime;
    private static final Long LONG_PRESS_ACTION_TIME = 2000L;

    public LongPressCircleView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LongPressCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LongPressCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.RED);
        circlePaint.setStrokeWidth(5);

//        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public void onLongPress(MotionEvent e) {
//                onLongPressEvent(e);
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager.getDefaultVibrator();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator = ContextCompat.getSystemService(context, Vibrator.class);
        } else {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    private void onLongPressEvent(MotionEvent e) {
        isLongPressTriggered = true;
        float x = e.getX();
        float y = e.getY();
        longPressCircle = new Circle(x, y, 20);
        invalidate();
        vibrate();
        startCircleAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isLongPressTriggered && longPressCircle != null) {
            canvas.drawCircle(longPressCircle.x, longPressCircle.y, longPressCircle.radius, circlePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressStartTime = SystemClock.uptimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                Long gapTime = SystemClock.uptimeMillis() - pressStartTime;
                if (gapTime >= LONG_PRESS_ACTION_TIME) {
                    onLongPressEvent(event);
                    if (!isLongPressTriggered) {
                        // 如果不是长按，重置状态
                        isLongPressTriggered = false;
                    } else {
                        // TODO
                    }
                }
                break;
            default:
                break;
        }
//        gestureDetector.onTouchEvent(event);
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (!isLongPressTriggered) {
//                isLongPressTriggered = false;
//            } else {
//                // TODO
//            }
//        }

        return true;
    }

    private void vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // 震动模式，这里设置为震动一次，持续时间50毫秒
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(vibrationEffect);
        } else {
            // 对于低于Android 8.0的版本，仍然使用旧的方式
            long[] pattern = {0, 50};
            vibrator.vibrate(pattern, -1);
        }
    }

    private void startCircleAnimation() {
        if (longPressCircle != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(longPressCircle, "radius", 20, 100);
            animator.setDuration(2000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    postInvalidate();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    longPressCircle = null;
                    isLongPressTriggered = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
    }

    private static class Circle {
        float x;
        float y;
        float radius;

        public Circle(float x, float y, float radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getRadius() {
            return radius;
        }
    }
}
