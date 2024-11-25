package com.example.selftest.filletpic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.selftest.R;

/**
 * @author LMH
 */
public class RoundCornerImageView extends AppCompatImageView {
    private float leftRadius;
    private float rightRadius;
    private Paint paint;
    private Path path;

    public RoundCornerImageView(Context context) {
        this(context, null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        leftRadius = typedArray.getDimension(R.styleable.RoundCornerImageView_leftRadius, 0);
        rightRadius = typedArray.getDimension(R.styleable.RoundCornerImageView_rightRadius, 0);
        typedArray.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.reset();
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        path.addRoundRect(rectF, new float[]{leftRadius, leftRadius, rightRadius, rightRadius, rightRadius, rightRadius, leftRadius, leftRadius}, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    public void setLeftRadius(float leftRadius) {
        this.leftRadius = leftRadius;
        invalidate();
    }

    public void setRightRadius(float rightRadius) {
        this.rightRadius = rightRadius;
        invalidate();
    }
}