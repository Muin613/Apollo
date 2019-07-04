package com.munin.music.ui.common;

import android.content.Context;
import android.view.ViewGroup;

import com.munin.library.view.widget.refreshlayout.LayoutManager;

/**
 * @author M
 */
public class RefreshLayoutManger extends LayoutManager {

    public RefreshLayoutManger(Context context) {
        super(context);
        RefreshHeader header = new RefreshHeader(mContext);
        ViewGroup.LayoutParams lp = header.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        }
        lp.height = 200;
        header.setLayoutParams(lp);
        setHeader(header);
        LoadFooter footer = new LoadFooter(mContext);
        lp = footer.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        }
        lp.height = 200;
        footer.setLayoutParams(lp);
        setFooter(footer);
    }


    @Override
    public int getRefreshReboundDuration() {
        return 1000;
    }

    @Override
    public int getLoadReboundDuration() {
        return 1000;
    }
}