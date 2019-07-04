package com.munin.library.view.widget.refreshlayout;

import android.content.Context;

import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshFooter;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshHeader;
import com.munin.library.view.widget.refreshlayout.interfaces.IRefreshLoadLayoutManager;

public abstract class LayoutManager implements IRefreshLoadLayoutManager {

    protected Context mContext;
    protected IRefreshFooter mFooter;
    protected IRefreshHeader mHeader;

    public LayoutManager(Context context) {
        this.mContext = context;
    }


    @Override
    public IRefreshFooter getFooter() {

        return mFooter;
    }

    @Override
    public void setFooter(IRefreshFooter footer) {
        mFooter = footer;
    }

    @Override
    public IRefreshHeader getHeader() {
        return mHeader;
    }

    @Override
    public void setHeader(IRefreshHeader header) {
        mHeader = header;
    }
}
