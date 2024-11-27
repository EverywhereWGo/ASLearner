package com.example.selftest.demo6;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;

import com.example.selftest.R;

import java.text.DecimalFormat;

/**
 * @author LMH
 * @date 17-2-20
 */

public class DownloadProgressButton extends androidx.appcompat.widget.AppCompatTextView {

    private Paint mBackgroundPaint;
    private volatile Paint mTextPaint;
    //背景颜色
    private int mBackgroundColor;
    //下载中后半部分后面背景颜色
    private int mBackgroundSecondColor;
    //文字颜色
    private int mTextColor;
    //覆盖后颜色
    private int mTextCoverColor;
    private float mButtonRadius;
    private float mBorderWidth;
    private float mProgress = -1;
    private float mToProgress;
    private int mMaxProgress;
    private int mMinProgress;
    private float mProgressPercent;
    //是否显示边框，默认是true
    private boolean showBorder;
    private RectF mBackgroundBounds;
    private LinearGradient mProgressTextGradient;

    //下载平滑动画
    private ValueAnimator mProgressAnimation;

    //记录当前文字
    private CharSequence mCurrentText;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_DOWNLOADING = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_FINISH = 3;
    private int mState;
    public DownloadProgressButton(Context context) {
        this(context, null);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initAttrs(context, attrs);
            init();
            setupAnimations();
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DownloadProgressButton);
        try {
            mBackgroundColor = a.getColor(R.styleable.DownloadProgressButton_progress_btn_background_color, Color.parseColor("#3385FF"));
            mBackgroundSecondColor = a.getColor(R.styleable.DownloadProgressButton_progress_btn_background_second_color, Color.parseColor("#E8E8E8"));
            mButtonRadius = a.getDimension(R.styleable.DownloadProgressButton_progress_btn_radius, 0);
            mTextColor = a.getColor(R.styleable.DownloadProgressButton_progress_btn_text_color, mBackgroundColor);
            mTextCoverColor = a.getColor(R.styleable.DownloadProgressButton_progress_btn_text_cover_color, Color.WHITE);
            mBorderWidth = a.getDimension(R.styleable.DownloadProgressButton_progress_btn_border_width, dp2px(2));
        } finally {
            a.recycle();
        }
    }

    private void init() {
        mMaxProgress = 100;
        mMinProgress = 0;
        mProgress = 0;
        showBorder = true;
        //设置背景画笔
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        //设置文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(50f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //解决文字有时候画不出问题
            setLayerType(LAYER_TYPE_SOFTWARE, mTextPaint);
        }
        //初始化状态设为NORMAL
        mState = STATE_NORMAL;
        invalidate();
    }

    private void setupAnimations() {
        //ProgressBar的动画
        mProgressAnimation = ValueAnimator.ofFloat(0, 1).setDuration(500);
        mProgressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float timePercent = (float) animation.getAnimatedValue();
                mProgress = ((mToProgress - mProgress) * timePercent + mProgress);
                invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode()) {
            drawing(canvas);
        }
    }

    private void drawing(Canvas canvas) {
        drawBackground(canvas);
        drawTextAbove(canvas);
    }

    private void drawBackground(Canvas canvas) {

        mBackgroundBounds = new RectF();
        //根据Border宽度得到Button的显示区域
        mBackgroundBounds.left = showBorder ? mBorderWidth : 0;
        mBackgroundBounds.top = showBorder ? mBorderWidth : 0;
        mBackgroundBounds.right = getMeasuredWidth() - (showBorder ? mBorderWidth : 0);
        mBackgroundBounds.bottom = getMeasuredHeight() - (showBorder ? mBorderWidth : 0);

        if (showBorder) {
            mBackgroundPaint.setStyle(Paint.Style.STROKE);
            mBackgroundPaint.setColor(mBackgroundColor);
            mBackgroundPaint.setStrokeWidth(mBorderWidth);
            canvas.drawRoundRect(mBackgroundBounds, mButtonRadius, mButtonRadius, mBackgroundPaint);
        }
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        //color
        switch (mState) {
            case STATE_NORMAL:
            case STATE_FINISH:
                mBackgroundPaint.setColor(mBackgroundColor);
                canvas.drawRoundRect(mBackgroundBounds, mButtonRadius, mButtonRadius, mBackgroundPaint);
                break;
            case STATE_PAUSE:
            case STATE_DOWNLOADING:
                //计算当前的进度
                mProgressPercent = mProgress / (mMaxProgress + 0f);
                mBackgroundPaint.setColor(mBackgroundSecondColor);
                canvas.save();
                //画出dst图层
                canvas.drawRoundRect(mBackgroundBounds, mButtonRadius, mButtonRadius, mBackgroundPaint);
                //设置图层显示模式为 SRC_ATOP
                PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
                mBackgroundPaint.setColor(mBackgroundColor);
                mBackgroundPaint.setXfermode(porterDuffXfermode);
                //计算 src 矩形的右边界
                float right = mBackgroundBounds.right * mProgressPercent;
                //在dst画出src矩形
                canvas.drawRect(mBackgroundBounds.left, mBackgroundBounds.top, right, mBackgroundBounds.bottom, mBackgroundPaint);
                canvas.restore();
                mBackgroundPaint.setXfermode(null);
                break;
            default:
                break;
        }
    }

    private void drawTextAbove(Canvas canvas) {
        //计算Baseline绘制的Y坐标
        final float y = canvas.getHeight() / 2 - (mTextPaint.descent() / 2 + mTextPaint.ascent() / 2);
        if (mCurrentText == null) {
            mCurrentText = "";
        }
        final float textWidth = mTextPaint.measureText(mCurrentText.toString());
        //color
        switch (mState) {
            case STATE_NORMAL:
                mTextPaint.setShader(null);
                mTextPaint.setColor(mTextCoverColor);
                canvas.drawText(mCurrentText.toString(), (getMeasuredWidth() - textWidth) / 2, y, mTextPaint);
                break;
            case STATE_PAUSE:
            case STATE_DOWNLOADING:
                //进度条压过距离
                float coverLength = getMeasuredWidth() * mProgressPercent;
                //开始渐变指示器
                float indicator1 = getMeasuredWidth() / 2 - textWidth / 2;
                //结束渐变指示器
                float indicator2 = getMeasuredWidth() / 2 + textWidth / 2;
                //文字变色部分的距离
                float coverTextLength = textWidth / 2 - getMeasuredWidth() / 2 + coverLength;
                float textProgress = coverTextLength / textWidth;
                if (coverLength <= indicator1) {
                    mTextPaint.setShader(null);
                    mTextPaint.setColor(mTextColor);
                } else if (indicator1 < coverLength && coverLength <= indicator2) {
                    //设置变色效果
                    mProgressTextGradient = new LinearGradient((getMeasuredWidth() - textWidth) / 2, 0, (getMeasuredWidth() + textWidth) / 2, 0,
                            new int[]{mTextCoverColor, mTextColor},
                            new float[]{textProgress, textProgress + 0.001f},
                            Shader.TileMode.CLAMP);
                    mTextPaint.setColor(mTextColor);
                    mTextPaint.setShader(mProgressTextGradient);
                } else {
                    mTextPaint.setShader(null);
                    mTextPaint.setColor(mTextCoverColor);
                }
                canvas.drawText(mCurrentText.toString(), (getMeasuredWidth() - textWidth) / 2, y, mTextPaint);
                break;
            case STATE_FINISH:
                mTextPaint.setColor(mTextCoverColor);
                canvas.drawText(mCurrentText.toString(), (getMeasuredWidth() - textWidth) / 2, y, mTextPaint);
                break;
            default:
                break;
        }

    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        //状态确实有改变
        if (mState != state) {
            this.mState = state;
            invalidate();
        }
    }

    /**
     * 设置当前按钮文字
     */
    public void setCurrentText(CharSequence charSequence) {
        mCurrentText = charSequence;
        invalidate();
    }


    /**
     * 设置带下载进度的文字
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setProgressText(String text, float progress) {
        if (progress >= mMinProgress && progress <= mMaxProgress) {
            DecimalFormat format = new DecimalFormat("##0.0");
            mCurrentText = text + format.format(progress) + "%";
            mToProgress = progress;
            if (mProgressAnimation.isRunning()) {
                mProgressAnimation.resume();
                mProgressAnimation.start();
            } else {
                mProgressAnimation.start();
            }
        } else if (progress < mMinProgress) {
            mProgress = 0;
        } else if (progress > mMaxProgress) {
            mProgress = 100;
            mCurrentText = text + progress + "%";
            invalidate();
        }
    }


    public boolean isShowBorder() {
        return showBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int width) {
        this.mBorderWidth = dp2px(width);
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
    }

    public float getButtonRadius() {
        return mButtonRadius;
    }

    public void setButtonRadius(float buttonRadius) {
        mButtonRadius = buttonRadius;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getTextCoverColor() {
        return mTextCoverColor;
    }

    public void setTextCoverColor(int textCoverColor) {
        mTextCoverColor = textCoverColor;
    }

    public int getMinProgress() {
        return mMinProgress;
    }

    public void setMinProgress(int minProgress) {
        mMinProgress = minProgress;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        SavedState ss = (SavedState) state;
//        super.onRestoreInstanceState(ss.getSuperState());
//        mState = ss.state;
//        mProgress = ss.progress;
//        mCurrentText = ss.currentText;
//    }
//
//    @Override
//    public Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        return new SavedState(superState, (int) mProgress, mState, mCurrentText.toString());
//    }
//
//    public static class SavedState extends BaseSavedState {
//
//        private int progress;
//        private int state;
//        private String currentText;
//
//        public SavedState(Parcelable parcel, int progress, int state, String currentText) {
//            super(parcel);
//            this.progress = progress;
//            this.state = state;
//            this.currentText = currentText;
//        }
//
//        private SavedState(Parcel in) {
//            super(in);
//            progress = in.readInt();
//            state = in.readInt();
//            currentText = in.readString();
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeInt(progress);
//            out.writeInt(state);
//            out.writeString(currentText);
//        }
//
//        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
//
//            @Override
//            public SavedState createFromParcel(Parcel in) {
//                return new SavedState(in);
//            }
//
//            @Override
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
//    }

    private int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
