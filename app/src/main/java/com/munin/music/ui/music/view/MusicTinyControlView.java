package com.munin.music.ui.music.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.munin.library.utils.ViewUtils;
import com.munin.library.view.widget.AspectImageView;
import com.munin.library.view.widget.ProgressView;
import com.munin.music.R;

/**
 * @author M
 */
public class MusicTinyControlView extends FrameLayout {

    private ProgressView mProgressView;
    private AspectImageView mControlImageView;

    public MusicTinyControlView(@NonNull Context context) {
        this(context, null);
    }

    public MusicTinyControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, null);
        if (mProgressView == null) {
            mProgressView = new ProgressView(context);
            mProgressView.setId(R.id.id_progress_view);
            mProgressView.setRoundWidth(3);
            mControlImageView = new AspectImageView(context);
            mControlImageView.setId(R.id.id_music_tiny_control_view);
            addView(mProgressView);
            addView(mControlImageView);
        }
        changePauseState();
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

    public void changePauseState() {
        if (mProgressView != null) {
            mProgressView.setRoundColor(Color.parseColor("#ffffff"));
            mProgressView.setBgColor(Color.parseColor("#9f9f9f"));
            mProgressView.postInvalidate();
        }
        ViewUtils.setImageSource(mControlImageView, R.drawable.drawable_music_tiny_play);
    }

    public void changePlayState() {
        if (mProgressView != null) {
            mProgressView.setBgColor(Color.parseColor("#ffffff"));
            mProgressView.setRoundColor(Color.parseColor("#9f9f9f"));
            mProgressView.postInvalidate();
        }
        ViewUtils.setImageSource(mControlImageView, R.drawable.drawable_music_tiny_pause);
    }
}
