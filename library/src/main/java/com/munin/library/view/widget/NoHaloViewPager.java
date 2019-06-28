package com.munin.library.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EdgeEffect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * @author M
 * 滑到最左和最右无光晕的ViewPager
 */
public class NoHaloViewPager extends ViewPager {
    private boolean mCanScroll = true;

    public NoHaloViewPager(@NonNull Context context) {
        super(context);
    }

    public NoHaloViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 移除光晕
     * 请在{@link #addOnPageChangeListener(OnPageChangeListener)}中OnPageChangeListener的onPageScrolled去实时调用
     */
    public void removeHalo() {
        EdgeEffect leftEdge = null, rightEdge = null;
        try {
            Class cls = ViewPager.class;
            Field leftEdgeField = cls.getDeclaredField("mLeftEdge");
            Field rightEdgeField = cls.getDeclaredField("mRightEdge");
            leftEdgeField.setAccessible(true);
            rightEdgeField.setAccessible(true);
            leftEdge = (EdgeEffect) leftEdgeField.get(this);
            rightEdge = (EdgeEffect) rightEdgeField.get(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (leftEdge == null || rightEdge == null) {
            return;
        }
        leftEdge.finish();
        rightEdge.finish();
        leftEdge.setSize(0, 0);
        rightEdge.setSize(0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCanScroll) {

            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mCanScroll) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }


    public void setCanScroll(boolean canScroll) {
        mCanScroll = canScroll;
    }
}
