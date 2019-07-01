package com.munin.library.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.munin.library.R;

/**
 * @author M
 */
public class ProgressView extends View {
    private Paint mArcPaint, mArcBgPaint;
    //长度
    private float mLength = 0;
    //圆弧的颜色
    private int mRoundColor;
    private int mRoundBgColor;
    //圆弧的弧线值
    private float mSweepValue = 0;
    private RectF mRectF;
    private int mProgressWidth;

    public ProgressView(Context context) {
        this(context,null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //1.获得自定义属性值的一个容器
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        //2.通过属性容器获得属性，如果没有就使用默认值
        mRoundColor = ta.getColor(R.styleable.ProgressView_progress_color, Color.rgb(255, 222, 0));
        mRoundBgColor = ta.getColor(R.styleable.ProgressView_progress_second_color, Color.rgb(255, 255, 255));
        mProgressWidth = ta.getInteger(R.styleable.ProgressView_progress_width, 10);
        //3.释放重复利用
        ta.recycle();
        initView();
        this.setRotation(-90);

    }


    private void initView() {
        mArcPaint = new Paint();
        mArcPaint.setStrokeWidth(mProgressWidth);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mRoundColor);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mArcBgPaint = new Paint();
        //宽度
        mArcBgPaint.setStrokeWidth(mProgressWidth);
        mArcBgPaint.setAntiAlias(true);
        mArcBgPaint.setColor(mRoundBgColor);
        mArcBgPaint.setStyle(Paint.Style.STROKE);
        mRectF = new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置View宽高的测量值
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        // 只有setMeasuredDimension调用之后，才能使用getMeasuredWidth()和getMeasuredHeight()来获取视图测量出的宽高，以此之前调用这两个方法得到的值都会是0
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        // 高度和宽度一样,取最小的那一个
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                childHeightSize, MeasureSpec.EXACTLY);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY);
        if (heightMeasureSpec > widthMeasureSpec) {
            heightMeasureSpec = widthMeasureSpec;
        } else {
            widthMeasureSpec = heightMeasureSpec;
        }
        mLength = getMeasuredWidth();
        mRectF.left = (float) (mLength * 0.1);
        mRectF.top = (float) (mLength * 0.1);
        mRectF.bottom = (float) (mLength * 0.9);
        mRectF.right = (float) (mLength * 0.9);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectF, 0, 360, false, mArcBgPaint);
        canvas.drawArc(mRectF, 0, mSweepValue, false, mArcPaint);
    }

    public void setProgress(float mSweepValue) {
        float a = (float) mSweepValue;
        if (a != 0) {
            this.mSweepValue = (float) (360.0 * (a / 100.0));
        } else {
            this.mSweepValue = 0;
        }
        postInvalidate();
    }

    public void setBgColor(int color) {
        mRoundBgColor = color;
        if ( mArcBgPaint == null) {
            return;
        }
        mArcBgPaint.setColor(mRoundBgColor);
    }

    public void setRoundColor(int color) {
        mRoundColor = color;
        if ( mArcPaint == null) {
            return;
        }
        mArcPaint.setColor(mRoundColor);
    }

    public void setRoundWidth(int width) {
        if (mArcBgPaint == null || mArcPaint == null) {
            return;
        }
        mProgressWidth = width;
        mArcBgPaint.setStrokeWidth(mProgressWidth);
        mArcPaint.setStrokeWidth(mProgressWidth);
    }
}