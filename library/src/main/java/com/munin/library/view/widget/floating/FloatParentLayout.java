package com.munin.library.view.widget.floating;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FloatParentLayout extends FrameLayout {
    View floatView;
    private boolean isFocusOnFloatView = false;

    public FloatParentLayout(@NonNull Context context) {
        super(context);
    }

    public FloatParentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (floatView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                Object tag = view.getTag();
                if (tag instanceof String) {
                    if ("floating".equals(tag)) {
                        floatView = view;
                        break;
                    }
                }
            }
            if (floatView != null) {
                floatView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        isFocusOnFloatView = true;
                        return false;
                    }
                });
                if (floatView instanceof ViewGroup) {
                    for (int i = 0; i < ((ViewGroup) floatView).getChildCount(); i++) {
                        ((ViewGroup) floatView).getChildAt(i).setOnLongClickListener(v -> {
                            isFocusOnFloatView = true;
                            return false;
                        });
                    }
                }
            }
        }
    }

    public void changeMoveFloatView(boolean isMove) {
        isFocusOnFloatView = isMove;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isFocusOnFloatView || super.onInterceptTouchEvent(event);
    }

    int x = 0;
    int y = 0;
    boolean isOnce = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFocusOnFloatView) {
            int h = 0;
            int w = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    h = floatView.getMeasuredHeight();
                    w = floatView.getMeasuredWidth();
                    floatView.setLeft((int) event.getX() + x);
                    floatView.setRight((int) event.getX() + x + w);
                    floatView.setTop((int) event.getY() + y);
                    floatView.setBottom((int) event.getY() + y + h);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isOnce) {
                        h = floatView.getMeasuredHeight();
                        w = floatView.getMeasuredWidth();
                        x = (floatView.getLeft() - (int) event.getX());
                        y = (floatView.getTop() - (int) event.getY());
                        floatView.setLeft((int) event.getX() + x);
                        floatView.setRight((int) event.getX() + x + w);
                        floatView.setTop((int) event.getY() + y);
                        floatView.setBottom((int) event.getY() + y + h);
                        isOnce = true;
                    } else {
                        h = floatView.getMeasuredHeight();
                        w = floatView.getMeasuredWidth();
                        floatView.setLeft((int) event.getX() + x);
                        floatView.setRight((int) event.getX() + x + w);
                        floatView.setTop((int) event.getY() + y);
                        floatView.setBottom((int) event.getY() + y + h);
                        if (event.getX() + x < 0) {
                            floatView.setLeft(0);
                            floatView.setRight(w);
                        }
                        if (event.getY() + y < 0) {
                            floatView.setTop(0);
                            floatView.setBottom(h);
                        }
                        if (event.getX() + x + w > getMeasuredWidth()) {
                            floatView.setLeft(getMeasuredWidth() - w);
                            floatView.setRight(getMeasuredWidth());
                        }
                        if (event.getY() + y + h > getMeasuredHeight()) {
                            floatView.setTop(getMeasuredHeight() - h);
                            floatView.setBottom(getMeasuredHeight());
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    h = floatView.getMeasuredHeight();
                    w = floatView.getMeasuredWidth();
                    if (event.getX() + x < 0 || event.getY() + y < 0 || event.getX() + x + w > getMeasuredWidth() || event.getY() + y + h > getMeasuredHeight()) {
                        isFocusOnFloatView = false;
                        isOnce = false;
                        break;
                    }
                    floatView.setLeft((int) event.getX() + x);
                    floatView.setRight((int) event.getX() + x + w);
                    floatView.setTop((int) event.getY() + y);
                    floatView.setBottom((int) event.getY() + y + h);
                    isFocusOnFloatView = false;
                    isOnce = false;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}
