package com.munin.library.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.munin.library.log.Logger;

/**
 * @author M
 */
public class ViewUtils {
    private static final String TAG = "ViewUtils";

    public static void setViewVisible(View view, int visible) {
        if (view == null) {
            return;
        }
        view.setVisibility(visible);
    }

    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static void removeView(View view) {
        if (view == null) {
            return;
        }
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    public static void setText(TextView view, String content) {
        if (view == null) {
            Logger.i(TAG,"setText: view is null?");
            return;
        }
        if (content == null) {
            content = "";
        }
        view.setText(content);
    }

    public static void setTextColor(TextView view, int color) {
        if (view == null) {
            return;
        }
        view.setTextColor(color);
    }

    public static void addView(ViewGroup parentView, View view) {
        if (view == null || parentView == null) {
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent != null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        parentView.addView(view, lp);
    }

    public static void setImageSource(ImageView view, int resId) {
        if (view == null) {
            return;
        }
        view.setImageResource(resId);
    }

    public static void setViewClickListener(View view, View.OnClickListener clickListener) {
        if (view == null) {
            return;
        }
        view.setOnClickListener(clickListener);
    }

    /**
     * dp转换成px
     */
    public static int dp2px(float dpValue) {
        Resources resources = getResources();
        if (resources == null) {
            Logger.e(TAG, "getDimenByRes: resources is null!");
            return (int) dpValue;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null) {
            return (int) (dpValue + 0.5f);
        }
        float scale = displayMetrics.density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换成dp
     */
    public static int px2dp(float pxValue) {
        Resources resources = getResources();
        if (resources == null) {
            Logger.e(TAG, "getDimenByRes: resources is null!");
            return (int) pxValue;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null) {
            return (int) (pxValue + 0.5f);
        }
        float scale = displayMetrics.density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(float spValue) {
        Resources resources = getResources();
        if (resources == null) {
            Logger.e(TAG, "getDimenByRes: resources is null!");
            return (int) spValue;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null) {
            return (int) (spValue + 0.5f);
        }

        float fontScale = displayMetrics.scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public static int px2sp(float pxValue) {
        Resources resources = getResources();
        if (resources == null) {
            Logger.e(TAG, "getDimenByRes: resources is null!");
            return (int) pxValue;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null) {
            return (int) (pxValue + 0.5f);
        }
        float fontScale = displayMetrics.scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getStatusHeight() {
        Resources resources = getResources();
        if (resources == null) {
            Logger.e(TAG, "getStatusHeight: resources is null!");
            return 0;
        }
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }


    private static Resources getResources() {
        Context context = ContextUtils.getApplicationContext();
        if (context == null) {
            return null;
        }
        return context.getResources();
    }
}
