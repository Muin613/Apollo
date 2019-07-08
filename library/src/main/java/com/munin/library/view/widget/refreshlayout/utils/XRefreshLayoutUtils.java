package com.munin.library.view.widget.refreshlayout.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.munin.library.log.Logger;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshHeader;

import java.lang.reflect.Field;

/**
 * @author M
 */
public class XRefreshLayoutUtils {
    private static final String TAG = "XRefreshLayoutUtils";

    public static boolean isShowHeader(@NonNull View refreshLayout, IRefreshHeader header) {
        if (header == null) {
            return false;
        }
        View view = header.getView();
        int[] location = new int[2];
        refreshLayout.getLocationOnScreen(location);
        int refreshY = location[1];
        view.getLocationOnScreen(location);
        int headerOnRefreshY = location[1];
        return headerOnRefreshY >= refreshY;
    }

    public static boolean canScrollVerticalOnAbsListView(@NonNull View targetView, int direction) {
        final ViewGroup viewGroup = (ViewGroup) targetView;
        final AbsListView absListView = (AbsListView) targetView;
        final int childCount = viewGroup.getChildCount();
        if (direction <= 0) {
            return childCount > 0 && (absListView.getLastVisiblePosition() < childCount - 1
                    || viewGroup.getChildAt(childCount - 1).getBottom() > targetView.getPaddingBottom());
        } else {
            return childCount > 0 && (absListView.getFirstVisiblePosition() > 0
                    || viewGroup.getChildAt(0).getTop() < targetView.getPaddingTop());
        }
    }

    public static boolean canScrollVerticalOnScrollingView(@NonNull ViewGroup targetView, int direction) {
        int scrollY = targetView.getScrollY();
        View child = targetView.getChildAt(0);
        if (child == null) {
            return true;
        }
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int parentTop = location[1];
        int parentY = location[1] + targetView.getMeasuredHeight();
        child.getLocationOnScreen(location);
        int childTop = location[1];
        int childY = location[1] + child.getMeasuredHeight();
        if (direction <= 0) {
            return childY <= parentY + 2;
        }
        return childTop == parentTop;
    }

    public static boolean canScrollVerticalOnRecyclerView(@NonNull RecyclerView targetView, int direction) {
        if (direction > 0) {
            return isRecyclerViewToTop(targetView);
        }
        return isRecyclerViewToBottom(targetView);
    }

    public static boolean isRecyclerViewToTop(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) {
            return true;
        }
        if (manager.getItemCount() == 0) {
            return true;
        }

        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;

            int firstChildTop = 0;
            if (recyclerView.getChildCount() > 0) {
                // 处理item高度超过一屏幕时的情况
                View firstVisibleChild = recyclerView.getChildAt(0);
                if (firstVisibleChild != null && firstVisibleChild.getMeasuredHeight() >= recyclerView.getMeasuredHeight()) {
                    return !recyclerView.canScrollVertically(-1);
                }
                // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top
                // 解决item的topMargin不为0时不能触发下拉刷新
                View firstChild = recyclerView.getChildAt(0);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) firstChild.getLayoutParams();
                firstChildTop = firstChild.getTop() - layoutParams.topMargin - getRecyclerViewItemTopInset(layoutParams) - recyclerView.getPaddingTop();
            }
            if (layoutManager.findFirstCompletelyVisibleItemPosition() < 1 && firstChildTop == 0) {
                return true;
            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
            int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
            if (out[0] < 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过反射获取RecyclerView的item的topInset
     *
     * @param layoutParams
     * @return
     */
    private static int getRecyclerViewItemTopInset(RecyclerView.LayoutParams layoutParams) {
        try {
            Field field = RecyclerView.LayoutParams.class.getDeclaredField("mDecorInsets");
            field.setAccessible(true);
            // 开发者自定义的滚动监听器
            Rect decorInsets = (Rect) field.get(layoutParams);
            return decorInsets.top;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isRecyclerViewToBottom(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null || manager.getItemCount() == 0) {
            return false;
        }

        if (manager instanceof LinearLayoutManager) {
            // 处理item高度超过一屏幕时的情况
            View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
            if (lastVisibleChild != null && lastVisibleChild.getMeasuredHeight() >= recyclerView.getMeasuredHeight()) {
                return !recyclerView.canScrollVertically(-1);
            }

            LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
            if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {

                // 处理BGAStickyNavLayout中findLastCompletelyVisibleItemPosition失效问题
                View lastCompletelyVisibleChild = layoutManager.getChildAt(layoutManager.findLastCompletelyVisibleItemPosition());
                if (lastCompletelyVisibleChild == null) {
                    return true;
                } else {
                    // 0表示x，1表示y
                    int[] location = new int[2];
                    lastCompletelyVisibleChild.getLocationOnScreen(location);
                    int lastChildBottomOnScreen = location[1] + lastCompletelyVisibleChild.getMeasuredHeight();
                    recyclerView.getLocationOnScreen(location);
                    int stickyNavLayoutBottomOnScreen = location[1] + recyclerView.getMeasuredHeight();
                    return lastChildBottomOnScreen <= stickyNavLayoutBottomOnScreen;
                }

            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;

            int[] out = layoutManager.findLastCompletelyVisibleItemPositions(null);
            int lastPosition = layoutManager.getItemCount() - 1;
            for (int position : out) {
                if (position == lastPosition) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canScrollVertical(@NonNull View targetView, int direction) {
        if (targetView instanceof AbsListView) {
            return canScrollVerticalOnAbsListView(targetView, direction);
        } else if (targetView instanceof RecyclerView) {
            return canScrollVerticalOnRecyclerView((RecyclerView) targetView, direction);
        } else if (targetView instanceof NestedScrollView || targetView instanceof ScrollView) {
            return canScrollVerticalOnScrollingView((ViewGroup) targetView, direction);
        } else {
            if (direction > 0) {
                Logger.i(TAG, "canScrollVertical : " + (targetView.getScrollY() > 0));
                return targetView.getScrollY() > 0;
            } else {
                Logger.i(TAG, "canScrollVertical : " + (targetView.getScrollY() < 0));
                return targetView.getScrollY() < 0;
            }
        }
    }

}
