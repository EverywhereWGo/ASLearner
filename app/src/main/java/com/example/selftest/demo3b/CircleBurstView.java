package com.example.selftest.demo3b;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LMH
 */
public class CircleBurstView extends View {

    private Paint circlePaint;
    private List<Circle> circles = new ArrayList<>();

    public CircleBurstView(Context context) {
        super(context);
        init();
    }

    public CircleBurstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleBurstView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.RED);
        circlePaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Circle circle : circles) {
            canvas.drawCircle(circle.x, circle.y, circle.radius, circlePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            Circle newCircle = new Circle(x, y, 0);
            circles.add(newCircle);

            invalidate();

            startCircleAnimation(newCircle);
        }

        return true;
    }

    private void startCircleAnimation(Circle circle) {
        // 使用属性动画来实现圆半径逐渐变大的效果
        ObjectAnimator animator = ObjectAnimator.ofFloat(circle, "radius", 0, 1500);
        animator.setInterpolator(new AccelerateInterpolator(0.6f));
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 每次属性值更新时触发视图重绘
                postInvalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后将圆从列表中移除
                circles.remove(circle);
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
