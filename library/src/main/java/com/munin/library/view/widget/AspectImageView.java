package com.munin.library.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class AspectImageView extends AppCompatImageView {
    public AspectImageView(Context context) {
        super(context);
    }

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
