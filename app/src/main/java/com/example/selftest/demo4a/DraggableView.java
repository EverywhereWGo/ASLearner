package com.example.selftest.demo4a;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author LMH
 */
public class DraggableView extends View {

    private int lastX;
    private int lastY;

    // 记录View的初始位置
    private int initLeft;
    private int initTop;

    // 屏幕的宽度和高度
    private int screenWidth;
    private int screenHeight;

    // 设置上下左右边界
    private int topBoundary;
    private int bottomBoundary;
    private int leftBoundary;
    private int rightBoundary;

    private Paint paint;

    public DraggableView(Context context) {
        super(context);
        init(context);
        initPaint();
    }

    public DraggableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initPaint();
    }

    public DraggableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initPaint();
    }

    private void init(Context context) {
        // 获取屏幕宽度和高度
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        // 设置初始边界（这里示例为距离屏幕边缘一定距离）
        topBoundary = 100;
        bottomBoundary = screenHeight - 100;
        leftBoundary = 100;
        rightBoundary = screenWidth - 100;
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(0xFF0000FF);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initLeft = left;
        initTop = top;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;

                // 计算新的位置
                int newLeft = getLeft() + offsetX;
                int newTop = getTop() + offsetY;

                // 限制在边界内
                newLeft = Math.max(leftBoundary, Math.min(newLeft, rightBoundary));
                newTop = Math.max(topBoundary, Math.min(newTop, bottomBoundary));

                // 设置新的位置
                layout(newLeft, newTop, newLeft + getWidth(), newTop + getHeight());

                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 这里可以进行自定义绘制内容，比如画个矩形示例
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    public Paint getPaint() {
        return paint;
    }
}