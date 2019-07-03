package com.munin.library.view.widget.refreshlayout.utils;

import android.view.View;

import androidx.annotation.NonNull;

import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshHeader;

public class XRefreshLayoutUtils {

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

}
