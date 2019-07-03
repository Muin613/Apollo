package com.munin.library.view.widget.refreshlayout.interfaces;

import android.content.Context;

/**
 * @author M
 */
public interface IRefreshLoadLayoutManager {

    IRefreshFooter getFooter();

    void setFooter(IRefreshFooter footer);

    IRefreshHeader getHeader();

    void setHeader(IRefreshHeader header);

    int getRefreshReboundDuration();

    int getLoadReboundDuration();

    boolean canTouchWhenRefreshOrLoad();

}
